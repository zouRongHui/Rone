<#assign path="${springMacroRequestContext.getContextPath()}">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>成功了</title>
</head>
<body>
    <h4>${content!'恭喜恭喜！！！！'}</h4>
    <br/>
    time: <#if time??>${time?string('yyyy-MM-dd HH:mm:ss')}</#if>
    <br/>
    others: ${others!}
    <br/>
    user: ${user!}
</body>
</html>