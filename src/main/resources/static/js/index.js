$(function () {
    $.ajax({
        type: 'GET',
        contentType: 'json',
        url: 'data/tables',
        success: function (result) {
            const $table_name = $('#table_name');
            $table_name.autocomplete({
                source: result.data,
                focus: function (event, ui) {
                    $table_name.val(ui.item.value);
                    return false;
                },
                select: function (r, ui) {
                    $table_name.val(ui.item.label)
                    $(`tr[id='${ui.item.label}']`).click();
                    locate()
                    return false;
                }
            })
        }
    })
})


const tableInfo = function (tableId, object) {
    $("#current").show();
    $("tr[onclick]").removeClass("bg-primary")
    $(object).addClass("bg-primary");
    const $filedBody = $("#filedBody");
    $('.tableFiled thead th').css('width', $filedBody.width() / 5 + 'px');
    $.ajax({
        type: 'GET',
        contentType: 'json',
        url: 'data/tables/' + tableId,
        success: function (result) {
            fillTable(result.data)
        },
        error: function () {
        }
    })
}

function fillTable(resultData) {
    const $filedBody = $("#filedBody");
    let tbody = '';
    resultData.forEach(function (v) {
        tbody += `<tr xmlns="http://www.w3.org/1999/html">
                                     <td class='filed-width'><label>${v.name}</label></td>
                                     <td class='filed-width'><label>${v.type}</label></td>
                                     <td class='filed-width'><label><input type="number" min="0" value="${v.length}"/></label></td>
                                     <td class='filed-width'><label>${v.nullAble}</label></td>
                                     <td class='filed-width'><label contenteditable>${v.comment}</label></td>
                              </tr>`;
    })
    $filedBody.text("");
    $filedBody.append(`<tr>
                                     <td class='filed-width'><label></label></td>
                                     <td class='filed-width'><label></label></td>
                                     <td class='filed-width'><label></label></td>
                                     <td class='filed-width'><label></label></td>
                                     <td class='filed-width'><label></label></td>
                              </tr>`);
    $filedBody.append(tbody)
    $("label").bind("blur", function () {
        const params = [];
        const tableName = $("tr[class='bg-primary']").children("td").eq(0).text().trim();
        $(this).parents("tr").children("td").each(function (i, v) {
            params.push($(v).text())
        })
        /*param 对应fillTable中五行数据 param[4]是 comment*/
        if (params[4] != null && params[4] !== '') {
            $.ajax({
                type: 'POST',
                dataType: "json",
                data: {tableName: tableName, tableColumn: params[0], comment: params[4]},
                url: 'opreation/commentOnColumn',
                success: function (result) {
                    //
                }
            })
        }
        /*param 对应fillTable中五行数据 param[4]是 comment*/
        if (params[3] != null && params[3] !== '') {
            console.log(params[3])
        }
    })
}


window.onscroll = function () {
    scrollFunction()
};

function scrollFunction() {
    const $top = $(`#top`);
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        $top.show()
    } else {
        $top.hide()
    }
}

// 点击按钮，返回顶部
function topFunction() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}

const locate = function () {
    $('html, body').animate({scrollTop: $('tr[class="bg-primary"]').offset().top - 200}, 100)
}

const modifyTableComment = function (obj) {
    let comment = $(obj).text().trim();
    const $table = $(obj).parents("tr");
    if (comment != null && comment !== '') {
        $.ajax({
            type: 'POST',
            dataType: "json",
            data: {tableName: $table.attr('id'), comment: comment},
            url: 'opreation/commentOnTable',
            success: function (result) {
                //
            }
        })
    }
}

const sql = function () {
    layer.open({
        type: 2,
        title: '在此处执行sql脚本，自动提交',
        shadeClose: true,
        shade: false,
        maxmin: true, //开启最大化最小化按钮
        area: ['80vw', '80vh'],
        content: '/sql'
    });
}
function exportDb() {
    window.open('/data/export/text')
}
function exportComment() {
    window.open('/comment/export/text')
}