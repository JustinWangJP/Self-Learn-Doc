# K8S学習豆知識のまとめ
1. ノード間通信の排除  
Podのロードバランシング確認（NodeとそのIP情報）
```
kubectl get pods -o custom-columns="Name:{metadata.name},Node:{spec.nodeName},NodeIP:{status.hostIP}"
```  
externalTrafficPolicyの設定による実現が可能。 
+ Cluster(デフォルト)：PODへの負荷を均等にする。
+ Local: ノードを跨いだロードバランシングを実施しない。
```
spec: 
  type: NodePort
  externalTrafficPolicy: Local
```

2. LoadBalancer Service作成
```
apiVersion: v1
kind: Service
metadata:
  name:  sample-lb
spec:
  type:  LoadBalancer
  loadBalancerIP: xxx.xxx.xxx.xxx
  ports:
  - name:  "http-port"
    protocol: TCP
    port:  80
    targetPort:  8080
  selector:
    app:  sample-app
  loadBalancerSourceRanges:                          #LoadBalancerのファイルウォールルールの設定
  - 10.0.0.0/8
```

3. Volume定義
+ emptyDir 
```
volumes:
- name: cache-volume
  emptyDir:{}
```

+ hostPath: ホスト上の任意領域をマウントすることができる。 

　type: 
  - Directory 
  - DirectoryOrCreate
  - File
  - Socket
  - BlockDevice
```
volumes:
- name: cache-volume
  hostPath:
      path: /etc
      type: DirectoryOrCreate  
```
+ downwardAPI 
```
volumes:
- name: cache-volume
  downwardAPI:
    items:
     - path: "podname"
       fieldRef:
        fieldPath: metadata.name
     - path: "cpu-request"
       resourceFieldRef:
        containerName: nginx-container
        resource: requests.cpu
```
+ projected: Secret/ConfigMap/downwardAPI/serviceAccountTokenのボリュームマウントを一箇所に集約する
```
volumes:
- name: projected-volume
  projected:
    sources:
    - secrete:
         name: sample-db-auth
         items:
            - key: username
              path: secrete/username.txt
    - configMap:
         name: sample-configmap
         items:
            - key: nginx.conf
              path: configmap/nginx.conf     
    - downwardAPI:
        items:
          - path: "podname"
            fieldRef:
              fieldPath: metadata.name
```

4. PersistentVolume作成
+ 属性の定義: 
  - ラベル
  - 容量：capacity
  - アクセスモード：accessModes
     + ReadWriteOnce(RWO)
     + ReadOnlyMany(ROX) : 複数のノードからRead可能
     + ReadWriteMany(RWX) : 複数のノードからRead/Write可能
  - Reclaim Policy:
     + Delete: PVの実体が削除される。
     + Retain: PVの実体が削除されない。
     + Recycle:　PVの実体が削除されないがファイル・フォールターが削除される。
  - マウントオプション：
  - StorageClass:　確認コマンド`kubectl get storageclasses`
  - プラグイン：gcePersistentDisk、nfs
  
+ Example: 
```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: sample-pv
  labels:
    type: gce-pv
    environment: stg
spec:
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteOnce
  persistentVolumeReclaimPolicy: Retain
  storageClassName: manual
  gcePersistentDisk:
    pdName: sample-pv-disk
    fsType: ext4
```

5. Namespace作成
+ マニフェスト作成方：
```
apiVersion: v1
kind: Namespace
metadata:
  name: sample-namespace
```
+ 実行CLI作成方:`kubectl create namespace sample-namespace`

6. LimitRangeによるリソース制限

| Type | 利用可能な設定項目 |
| :--- | :--- |
| Container | default/defaultRequest/max/min/maxLimitRequestRatio |
| Pod| max/min/maxLimitRequestRatio|
| PVC| max/min|

7. オートスケーリング3種類
+ Cluster Autoscaler: Podを起動できるノードが存在しない場合、ノードを新規追加
+ HorizontalPodAutoscaler: Podのレプリカ数を負荷に応じて自動的に増減
+ VerticalPodAutoscaler: Podに割り当てられているリソース(Request/Limits)を負荷に応じて自動的に増減

```
# HorizontalPodAutoscalerのマニフェスト定義例
apiVersion: autoscaling/v2beta1
kind: HorizontalPodAutoscaler
metadata:
  name: sample-hpa
  namespace: default
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: sample-deployment
  minReplicas:1
  maxReplicas:10
  metrics:
  - type: Resource
    resource:
      name: cpu
      targetAverageUtilization: 50
```

実行CLI例:`kubectl autoscale deployment sample-deployment --cpu-percent=50 --min=1 --max=10`
