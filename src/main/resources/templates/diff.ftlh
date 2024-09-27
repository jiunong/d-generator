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
    <script type="text/javascript">
        function diff() {
            let data = {
                "text1": $("#text1").val(),
                "text2": $("#text2").val(),
                "token": "sict"
            }
            $.post("/diffData", data, function (res) {
                console.log(res)
            }, 'json/application')
        }

    </script>
</head>
<body>
<div id="root" class="root container-fluid">
    <div class="row clearfix">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6">
                    <label>
                        <textarea id="text1" class="diff_textarea" rows="10">为保障 e计划及桌面终端管理系统(包含网络控制准入)两套系统安全稳定的运行提高数据处理、系统快速响应的服务能力，提高用系统的交瓦性能，使系统用户获得较好的使用体验，实现系统巡检、应用系统升级、系统安全支撑、事件及故障处理、集成数据运维、特殊保障、运行监测及优化、运维配置信息维护、日常应用巡检等工作内容，并通过按照信息运维服务“标准化、规范化、精益化”管理的要求，加强信息系统巡检统一集中管控能力，提高运维服务管理水平，保障系统安全、稳定运行，进一步加强系统运维管理，完善运维体系,为用户提供高质量的信息系统保障。“</textarea>
                    </label>
                </div>
                <div class="col-md-6">
                    <label>
                        <textarea id="text2" class="diff_textarea" rows="10">为保障e计划、桌面终端管理系统(包含网络控制准入)2 套系统继续安全稳定的运行，实现系统巡检、应用系统升级、系统安全支撑、事件及故障处理集成数据运维、特殊保障、运行监测及优化、运维配置信息维护、日常应用巡检等工作内容，系统巡检成为保障信息系统稳定运行重要手段，通过按照信息运维服务“标准化、规范化、精益化” 管理的要求，加强信息系统巡检统一集中管控能力，提高运维服务管理水平，保障系统安全、稳定运行，进一步加强系统运维管理，完善运维体系,为用户提供高质量的信息系统保障。</textarea>
                    </label>
                </div>
            </div>
            <div>
                <button onclick="diff()" value="DIFF">DIFF</button>
            </div>
            <div class="row">
                result
            </div>
        </div>
    </div>
</div>
</body>
</html>
