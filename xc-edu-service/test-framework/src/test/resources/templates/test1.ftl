<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>资产</td>
    </tr>
    <#list stus as li>
        <tr>
            <td>${li_index+1}</td>
            <td>${li.name}</td>
            <td>${li.age}</td>
            <td>${li.money}</td>
        </tr>
    </#list>
</table>
<br>
遍历map<br>
<#-- 这里的stu是遍历的 mapstu这个键对应值得键-->
<#list mapstu?keys as stu>
<#-- map的获取数据的方式有两种方式
    mapstu.键名.属性名
    mapstu[键名].属性名
 -->
姓名：${mapstu[stu].name}
年龄：${mapstu[stu].age}
</#list>

</body>
</html>