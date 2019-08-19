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
