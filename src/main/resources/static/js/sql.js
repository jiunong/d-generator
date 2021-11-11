/**
 *  屏蔽原始右键事件
 */
window.oncontextmenu = function (event) {
    event.preventDefault();
}
// 鼠标右键单击事件
;(function ($) {
    $.fn.extend({
        // 定义鼠标右键方法,接收一个函数参数
        "rightClick": function (fn) {
            // 调用这个方法后将禁止系统的右键惨淡
            $(document).on('contextmenu', function (e) {
                return false;
            });
            // 为这个对象绑定鼠标按下事件
            $(this).mousedown(function (e) {
                // 如果按下的是右键，则执行函数
                if (e.which === 3) {
                    fn();
                }
            })
        }
    });
})(jQuery);

$(function () {
    $('#textarea_sql').rightClick(function (e) {
        let txt = window.getSelection().toString();
        if(!txt){return false;}
        //询问框
        layer.confirm(`是否执行{${txt}}`, {
            btn: ['执行', '取消'] //按钮
        }, function () {
            $.ajax({
                type: 'GET',
                contentType: 'json',
                data: {sql: txt},
                url: 'sql/execute',
                success: function (result) {
                    $('#sql').val(txt);
                    $("#textarea_result").html(prettyFormat(JSON.stringify(result)))
                    if (result.code !== 1001) {
                        $('.M-box1').pagination({
                            totalData: result.code,
                            showData: 20,
                            coping: true,
                            callback: function (api) {
                                let txt = window.getSelection().toString();
                                let data = {
                                    showData: 20,
                                    page: api,
                                    sql: txt ? txt : $('#sql').val()
                                };
                                $.getJSON('sql/execute', data, function (result) {
                                    $("#textarea_result").html(prettyFormat(JSON.stringify(result)))
                                });
                            }
                        });
                    }


                }
            })
            layer.msg('执行中', {icon: 1});
        }, function () {
            layer.msg('取消');
        });
    })
});


function fillTable(data) {
    let tr = `<tr>`
    data.forEach(function (v) {
        for (let item in v) {
            tr += `<td>${v[item]}</td>`;
        }
        tr += `</tr>`
    })
    let $tableResult = $("#table_result");

    $tableResult.find('thead')
    $tableResult.find('tbody').text("")
    $tableResult.find('tbody').append(tr)
}


//json格式美化
function prettyFormat(str) {
    try {
        // 设置缩进为2个空格
        str = JSON.stringify(JSON.parse(str), null, 2);
        str = str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');
        return str.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            /*let cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';*/
            return match;
        });
    } catch (e) {
        alert("异常信息:" + e);
    }
}

