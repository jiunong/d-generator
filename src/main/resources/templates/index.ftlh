<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>INDEX</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- 新 Bootstrap4 核心 CSS 文件 -->
    <link rel="stylesheet" href="css/bootstrap_4.1.0.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/jquery-ui-1.12.1/jquery-ui.css">
    <script src="js/jquery.js"></script>
    <script src="js/jquery-ui.js"></script>
    <script src="layer/layer.js"></script>
    <script src="js/index.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row clearfix">
        <div class="col-md-4 column">
            <table class="table table-bordered table-condensed table-striped table-hover">
                <thead class="fixThead">
                <tr>
                    <th><button id="_sql" onclick="sql()">sql执行窗口</button></th>
                    <th>
                        <input type="text" id="table_name" name="table_name" placeholder="请输入表名"/>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><label></label></td>
                    <td><label></label></td>
                </tr>
                <#list data as item>
                    <tr id="${item.tableName}" onclick="tableInfo('${item.tableId}',this)">
                        <td><label >
                                ${item.tableName}
                            </label></td>
                        <td><label contenteditable onblur="modifyTableComment(this)">
                                ${item.comment!""}
                            </label></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="col-md-8 column tableFiled">
            <table class="table table-bordered table-condensed table-striped ">
                <thead>
                <tr>
                    <th>NAME</th>
                    <th>TYPE</th>
                    <th>LENGTH</th>
                    <th>NULLABLE</th>
                    <th>COMMENT</th>
                </tr>
                </thead>
                <tbody id="filedBody">
                </tbody>
            </table>
        </div>
    </div>
</div>
<button onclick="locate()" class="bottom_button" id="current" title="回选中表">CURRENT</button>
<button onclick="topFunction()" class="bottom_button" id="top" title="回顶部">TOP</button>
</body>
</html>