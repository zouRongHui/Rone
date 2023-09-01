

### 基础
[参考文档](https://www.runoob.com/w3cnote/nginx-install-and-config.html)
[参考文档](https://www.runoob.com/w3cnote/nginx-setup-intro.html)
[参考文档](https://www.nginx.cn/doc/)
* nginx 的常规用途：静态文件服务器、反向代理服务器、负载均衡

* 应用场景
  * 方向代理
  * 负载均衡
  * http服务器
  * 正向代理

* nginx 配置
  * 全局块：配置影响nginx全局的指令。一般有运行nginx服务器的用户组，nginx进程pid存放路径，日志存放路径，配置文件引入，允许生成worker process数等。
  * events块：配置影响nginx服务器或与用户的网络连接。有每个进程的最大连接数，选取哪种事件驱动模型处理连接请求，是否允许同时接受多个网路连接，开启多个网络连接序列化等。
  * http块：可以嵌套多个server，配置代理，缓存，日志定义等绝大多数功能和第三方模块的配置。如文件引入，mime-type定义，日志自定义，是否使用sendfile传输文件，连接超时时间，单连接请求数等。
  * server块：配置虚拟主机的相关参数，一个http中可以有多个server。
  * location块：配置请求的路由，以及各种页面的处理情况。
  ```
  配置文件结构
      ...              #全局块
      events { ... } #events块
      http {     #http块
          ...   #http全局块
          server {        #server块
              ...       #server全局块
              location [PATTERN] { ... } #location块
              location [PATTERN] { ... }
          }
          server { ...  }
          ...     #http全局块
      }
  详见 config 示例
  ```


************************************************************************************************************************
### 坑
* 问题：大于 128k 的文件上传一直失败
  * 原因：nginx反向代理配置了下面两个参数，当请求数据大于128k时请求数据会存放到 client_body_temp 文件中，
    而当前启用nginx服务的用户没有该文件的操作权导致的问题。
    client_max_body_size    160m; # 请求体最大数据量
    client_body_buffer_size 128k; # 将请求数据存储到内存中的阈值
  * 解决方案：更换有权限的用户启用nginx服务 or 给用户配置上权限


************************************************************************************************************************
### config 示例
```
#user  root; #配置用户或者组，默认为nobody nobody。
worker_processes  1; #允许生成的进程数，默认为1

#制定日志路径，级别。这个设置可以放入全局块，http块，server块，级别以此为：debug|info|notice|warn|error|crit|alert|emerg
#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid; #指定nginx进程运行文件存放地址

events {
    accept_mutex on;   #设置网路连接序列化，防止惊群现象发生，默认为on。惊群现象：一个网路连接到来，多个睡眠的进程被同时叫醒，但只有一个进程能获得链接，这样会影响系统性能。
    multi_accept off;  #设置一个进程是否同时接受多个网络连接，默认为off
    #use epoll;      #事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
    worker_connections  1024; #最大连接数，默认为512
}

http {
    include       mime.types; #文件扩展名与文件类型映射表
    default_type  application/octet-stream; #默认文件类型，默认为text/plain
    #access_log off; #取消服务日志
    #自定义格式
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main; #combined为日志格式的默认值

    sendfile        on; #允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
    #sendfile_max_chunk 100k;  #每个进程每次调用传输数量不能大于设定的值，默认为0，即不设上限。
    #tcp_nopush     on;

    keepalive_timeout  65; #连接超时时间，默认为75s，可以在http，server，location块。

    #error_page 404 https://www.baidu.com; #错误页

    #gzip  on;
    include conf.d/*.conf; #加载别的配置文件到当前位置

    proxy_redirect          off;
    proxy_set_header        Host            $host;
    proxy_set_header        X-Real-IP       $remote_addr;
    proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
    client_max_body_size    160m;
    client_body_buffer_size 128k;
    proxy_connect_timeout   900; #nginx与upstream server的连接超时时间
    proxy_send_timeout      900; #nginx发送数据至upstream server超时, 默认60s, 如果连续的60s内没有发送1个字节, 连接关闭
    proxy_read_timeout      900; #nginx接收upstream server数据超时, 默认60s, 如果连续的60s内没有收到1个字节, 连接关闭
    proxy_buffers           4 128k;

    upstream manager_server {
        server localhost:8480;
    }
    upstream wechat_server {
        server localhost:8280;
    }
    upstream test_wechat_server {
        server localhost:8288;
    }
    upstream file_server {
        server localhost:8880;
    }
    upstream local_server {
        server localhost:8280;
    }

    server {
        #keepalive_requests 120; #单连接请求上限次数。
        listen       8180; #监听端口
        server_name  www.rone.com; #监听地址
        access_log logs/manager.access.log main;

        location /rone { #请求的url过滤，正则匹配，~为区分大小写，~*为不区分大小写。
            root  /app; #根目录
            #index vv.txt;  #设置默认页
        }
        location /f/ {
            proxy_pass       http://file_server; #请求转向mysvr 定义的服务器列表
            deny 127.0.0.1; #拒绝的ip
            allow 172.18.5.54; #允许的ip
            ## 配置请求支持跨域
            add_header Access-Control-Allow-Origin *; #服务器可以接受所有的请求源
            add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
            add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
            ## 配置请求支持跨域结束
            proxy_set_header Host $host:$server_port;
            proxy_set_header X-Forwarded-Host $host:$server_port;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto https;
        }
        location /images/newindex/logo.png {
            return 200 'OK';
        }
        location / {
            proxy_pass       http://manager_server;
            proxy_set_header Host $host:$server_port;
            proxy_set_header X-Forwarded-Host $host:$server_port;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto https;
        }
        location /WW_verify_pdAbsjQxE4bhYbeP.txt {
        return 200 'pdAbsjQxE4bhYbeP';
        }
    }

    server {
        listen 18810;
        server_name www.rone.com;
        access_log logs/manager.access.log main;

        location / {
            proxy_pass       http://127.0.0.1:8180;
            proxy_set_header Host $host:$server_port;
            proxy_set_header X-Forwarded-Host $host:$server_port;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto https;
        }
    }

    server {
        listen       1443;
        server_name  www.rone.com;
        access_log   logs/wechat.access.log main;

        location / {
            proxy_pass       http://127.0.0.1:8280;
            proxy_set_header Host $host:$server_port;
            #proxy_set_header X-Forwarded-Host 127.0.0.1:1443;
            proxy_set_header X-Forwarded-Host $host:$server_port;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto https;
        }
        location /WW_verify_pdAbsjQxE4bhYbeP.txt {
            return 200 'pdAbsjQxE4bhYbeP';
        }
        location /.well-known/pki-validation/fileauth.txt {
            return 200 '202008160000003gg6ce3quhzv47ybqpur1fsoqoqe1kf41fg6h69iroh5beu0tp';
        }
        location /rone.txt {
            return 200 'hello java';
        }
    }

    server {
        listen       2443;
        server_name  www.rone.com;
        access_log   logs/wechat.access.log main;

        location / {
            proxy_pass       http://local_server;
            proxy_set_header Host $host:$server_port;
            proxy_set_header X-Forwarded-Host $host:$server_port;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto https;
        }
        location /rone.txt {
            return 200 'hello world';
        }
    }

    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```
