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
