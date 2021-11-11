<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>DATABASE-INIT</title>
    <link rel="icon" href="favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/pagination.css">
    <script src="js/jquery.js"></script>
    <script src="layer/layer.js"></script>
    <script src="js/jquery.pagination.js"></script>
    <script src="js/sql.js"></script>
</head>
<body>
<input id ="sql" style="display: none"/>
<div id="root" class="root container-fluid">
    <div class="row clearfix">
        <div style="background: #cacad4">
            <label>
                <textarea id="textarea_sql" placeholder="请在此处写sql"
                          style="width: 99vw;height: 25vh;resize: vertical"></textarea>
            </label>
        </div>
        <div style="background: #cacad4">
            <label>
                <textarea id="textarea_result" placeholder="请在此处写sql" style="width: 99vw;height: 66vh;resize: none"></textarea>
            </label>
            <div class="M-box1"></div>

        </div>
    </div>
</div>
</body>
</html>