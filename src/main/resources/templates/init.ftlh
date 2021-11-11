<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>DATABASE-INIT</title>
    <link rel="icon" href="favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="css/bootstrap_4.1.0.css">
    <link rel="stylesheet" href="css/common.css">
    <script src="js/jquery.js"></script>
    <script src="layer/layer.js"></script>
    <script src="js/init.js"></script>
</head>
<body class="body-bg">
<div id="root" class="root container-fluid">
    <div class="row clearfix">
        <div class="col-md-8 column init">
            <div class="row">
                <div class="col-md-5">
                    <ul class="db-list" style="font-size: x-large;color: white">
                        <#list dbs as db>
                            <li onclick="chooseDb(this)"
                                description="${db.description}"
                                url="${db.url}"
                                database="${db.database}"
                                username="${db.username}"
                                password="${db.password}"
                                schemaName="${db.schemaName}">${db.description}</li>
                        </#list>
                    </ul>
                </div>
                <div class="col-md-7">
                    <form id="form_table" action="/toIndexP" method="post">
                        <table class="table table-borderless">
                            <tbody>
                            <tr>
                                <td width="20%"><label>DESCRIPTION</label></td>
                                <td><label>
                                        <input name="description" type="text"
                                               value="沈阳配网检修">
                                    </label></td>
                            </tr>
                            <tr>
                                <td><label>URL</label></td>
                                <td><label>
                                        <input name="url" type="text"
                                               value="jdbc:dm://192.168.135.37:12345/OMSPDJX?allowMultiQueries=true">
                                    </label></td>
                            </tr>
                            <tr>
                                <td><label>DATABASE</label></td>
                                <td><label>
                                        <input name="database" type="text" value="OMSPDJX">
                                    </label></td>
                            </tr>
                            <tr>
                                <td><label>USERNAME</label></td>
                                <td><label>
                                        <input name="username" type="text" value="OMSPDJXSY">
                                    </label></td>
                            </tr>
                            <tr>
                                <td><label>PASSWORD</label></td>
                                <td><label>
                                        <input name="password" type="text" value="OMSPDJXSY">
                                    </label></td>
                            </tr>
                            <tr>
                                <td><label title="为唯一属性，列表存在则更新，否则新增">SCHEMA</label></td>
                                <td><label>
                                        <input name="schemaName" type="text" value="OMSPDJX_SY">
                                    </label></td>
                            </tr>
                            <tr>
                                <td><label>
                                        <button id="_confirm" type="submit" class="but">确定连接<span></span></button>
                                    </label></td>
                                <td><label>
                                        <button id="_modify" onclick="modifyDb()" type="button" class="but">更新配置<span></span></button>
                                    </label></td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>