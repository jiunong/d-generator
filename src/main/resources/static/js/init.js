const chooseDb = function (obj) {
    const $li = $(obj);
    $li.addClass("bg-info")
    $li.siblings().removeClass("bg-info")
    $("input[name='description']").val(obj.getAttribute("description"))
    $("input[name='url']").val(obj.getAttribute("url"))
    $("input[name='database']").val(obj.getAttribute("database"))
    $("input[name='username']").val(obj.getAttribute("username"))
    $("input[name='password']").val(obj.getAttribute("password"))
    $("input[name='schemaName']").val(obj.getAttribute("schemaName"))
}


const modifyDb = function () {
    const formData = $("#form_table").serialize();
    $.ajax({
        type: 'POST',
        async:true,
        dataType: "json",
        data: formData,
        url: 'datasource/update',
        success: function (result) {
            layer.msg(result.data, {
                btn: ['明白了'], //按钮
                btnAlign: 'c'
            }, function(){
                window.location.reload()
            });
        }
    })
}

/**
 *  屏蔽原始右键事件
 */
window.oncontextmenu = function (event) {
    event.preventDefault();
}
