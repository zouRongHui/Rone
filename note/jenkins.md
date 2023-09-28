
### 流水线
#### pipeline script
示例代码包含有 svn更新源码、maven编译打包、启动服务(配合ansible脚本实现)、服务心跳检测(配合ansible脚本实现)
```
// 该函数用于创建一个该流水线脚本下全局的备份标签时间戳
def createBackupTag() {
    return "bak_" + new Date().format('yyyyMMddHHmmss')
}
pipeline {
    agent any
    options {
        timestamps()
        // 整体超时时间
        timeout(time: 10, unit: 'MINUTES')
        // 并发时有一个stage失败会导致整个任务立刻失败
        parallelsAlwaysFailFast()
    }
    // 环境变量
    environment{
        // 需要使用到的maven版本和目录
        mvn_home = "/home/jenkins/apache-maven-3.5.4/bin"
        // svn代码下载的用户及凭据
        p_svnUser ="svnUser"
        // 本地代码路径
        p_proj_path = "/home/jenkins/source_code/rone"
        // 备份时间戳
        bak_tag = createBackupTag()
        // ansible脚本路径
        ansible_home = "/home/jenkins/ansible/rone"
    }
    // 发布时配置的参数，已实现某些动态参数的控制、哪些步骤是否执行的控制
    parameters {
        string(name: 'srcCodeUrl', defaultValue: '', description: '请输入项目的源码仓库URL地址')
        booleanParam(name: 'package', defaultValue: true, description: "是否编译打包")
        booleanParam(name: 'server1', defaultValue: true, description: "是否要发布 192.168.0.1 服务器")
        booleanParam(name: 'server2', defaultValue: true, description: "是否要发布 192.168.0.2 服务器")
    }
    stages {
        stage("SVN代码检出") {
        	// 选择了[是]的情况下，继续执行下面的steps，否则下面的步骤不执行
            when { equals expected: true, actual: params.package }
            steps{
                script{
                    checkout([
                        $class: 'SubversionSCM',
                        additionalCredentials: [],
                        excludedCommitMessages: '',
                        excludedRegions: '',
                        excludedRevprop: '',
                        excludedUsers: '',
                        filterChangelog: false,
                        ignoreDirPropChanges: false,
                        includedRegions: '',
                        locations: [[
                            cancelProcessOnExternalsFail: true,
                            credentialsId: "${p_svnUser}",
                            depthOption: 'infinity',
                            ignoreExternalsOption: true,
                            local: '../' + "rone" ,
                            remote: "${srcCodeUrl}"
                        ]],
                        quietOperation: true,
                        workspaceUpdater: [$class: 'UpdateUpdater']
                    ])
               }
            }
        }

        stage("测试环境文件覆盖，该步骤只能使用于测试环境") {
            when { equals expected: true, actual: params.package }
            steps{
                script{
                    sh "cp /home/rone/application.yml  /home/jenkins/source_code/rone/src/main/resources/application.yml"
                }
            }
        }

        stage("编译打包") {
            when { equals expected: true, actual: params.package }
            steps{
                script{
                    // maven编译打包命令
                    sh "${mvn_home}/mvn -f ${p_proj_path}/pom.xml -Dmaven.test.skip=true -Pdev clean package"
                }
            }
        }

        stage("更新发布 192.168.0.1") {
            when { equals expected: true, actual: params.server1 }
            steps {
                // 定义了发布的ansible脚本，调用指定ansible工作目录下的hosts文件和playbook脚本
                ansiblePlaybook (
                    inventory: '${ansible_home}/hosts',
                    extras: '--extra-vars "deploy_hosts=server1 bak_tag=${bak_tag}"',
                    playbook: '${ansible_home}/deploy.yml'
                )
            }
        }

        stage("更新发布 192.168.0.2") {
            when { equals expected: true, actual: params.server2 }
            steps {
                ansiblePlaybook (
                    inventory: '${ansible_home}/hosts',
                    extras: '--extra-vars "deploy_hosts=server2 bak_tag=${bak_tag}"',
                    playbook: '${ansible_home}/deploy.yml'
                )
            }
        }

        stage("心跳检测 192.168.0.1") {
            when { equals expected: true, actual: params.server1 }
            steps {
                ansiblePlaybook (
                    inventory: '${ansible_home}/hosts',
                    extras: '--extra-vars "deploy_hosts=server1"',
                    playbook: '${ansible_home}/check-deploy.yml'
                )
            }
        }

        stage("心跳检测 192.168.0.2") {
            when { equals expected: true, actual: params.server2 }
            steps {
                ansiblePlaybook (
                    inventory: '${ansible_home}/hosts',
                    extras: '--extra-vars "deploy_hosts=server2"',
                    playbook: '${ansible_home}/check-deploy.yml'
                )
            }
        }
    }
}
```

#### ansible语法
* hosts 配置文件，配置应用服务器的登录信息
```
[server1]
192.168.0.1 ansible_ssh_pass="123"
[server2]
192.168.0.2 ansible_ssh_pass="123"
```
* 服务发布脚本 deploy.yml
```
---
- hosts: "{{ deploy_hosts }}"
  remote_user: rone
  vars:
    app_dir: /app/rone
    backup_dir: /app/rone/backup
    service1: rone-job.jar
    service2: rone-manager.jar
    service3: rone-wechat.jar
    source_code_dir: /home/jenkins/source_code/rone
  tasks:
    - name: "检查待发布的job包是否存在step1"
      local_action: stat path="{{source_code_dir}}/rone-job/target/{{service1}}"
      register: stat_result
    - name: "检查待发布的job包是否存在step2"
      fail: msg="要发布的job包不存在，异常退出"
      when: not stat_result.stat.exists
    - name: "检查待发布的manager包是否存在step1"
      local_action: stat path="{{source_code_dir}}/rone-manager/target/{{service2}}"
      register: stat_result
    - name: "检查待发布的manager包是否存在step2"
      fail: msg="要发布的manager包不存在，异常退出"
      when: not stat_result.stat.exists
    - name: "检查待发布的wechat包是否存在step1"
      local_action: stat path="{{source_code_dir}}/rone-wechat/target/{{service3}}"
      register: stat_result
    - name: "检查待发布的wechat包是否存在step2"
      fail: msg="要发布的wechat包不存在，异常退出"
      when: not stat_result.stat.exists

    - name: "停服"
      shell: "sh /app/rone/stop.sh"

    - name: "创建备份文件夹"
      shell: mkdir {{backup_dir}}/{{bak_tag}}
    - name: "备份job"
      shell: mv {{app_dir}}/{{service1}} {{backup_dir}}/{{bak_tag}}/{{service1}}
    - name: "备份manager"
      shell: mv {{app_dir}}/{{service2}} {{backup_dir}}/{{bak_tag}}/{{service2}}
    - name: "备份wechat"
      shell: mv {{app_dir}}/{{service3}} {{backup_dir}}/{{bak_tag}}/{{service3}}

    - name: "获取最新的job包"
      copy:
        src={{source_code_dir}}/rone-job/target/{{service1}}
        dest={{app_dir}} mode=0764
    - name: "获取最新的manager包"
      copy:
        src={{source_code_dir}}/rone-manager/target/{{service2}}
        dest={{app_dir}} mode=0764
    - name: "获取最新的wechat包"
      copy:
        src={{source_code_dir}}/rone-wechat/target/{{service3}}
        dest={{app_dir}} mode=0764

    - name: "启服务"
      shell: nohup sh /app/rone/start.sh &
```
* 心跳检测脚本 check-deploy.yml
```
---
- hosts: "{{ deploy_hosts }}"
  remote_user: rone
  vars:
    service1: rone-job.jar
    service2: rone-manager.jar
    service3: rone-wechat.jar
  tasks:
    - name: "检查job服务是否启动"
      uri:
        url: http://{{ deploy_hosts }}:8380/favicon.ico
        method: GET
        return_content: yes
      register: response
      until: response.status == 200	# 停止循环的条件
      retries: 60	# 等待循环之间等待的时长，单位秒
      delay: 5	# 循环次数
      when: ansible_default_ipv4.address == "192.168.0.1"	# 只在 192.168.0.1 这台服务器上才去检测job服务
    - name: "检查manager服务是否启动"
      uri:
        url: http://{{ deploy_hosts }}:8180/favicon.ico
        method: GET
        return_content: yes
      register: response
      until: response.status == 200
      retries: 60
      delay: 5
    - name: "检查wechat服务是否启动"
      uri:
        url: http://{{ deploy_hosts }}:8280/favicon.ico
        method: GET
        return_content: yes
      register: response
      until: response.status == 200
      retries: 60
      delay: 5
```