$(function () {
    $(window).resize(function () {
        $("body").removeClass("layadmin-side-spread-sm").removeClass("layadmin-side-shrink");
        $("a[layadmin-event=flexible]").removeClass("layui-icon-spread-left").addClass("layui-icon-spread-right");
    });
    $(".layadmin-body-shade").click(function () {
        $(".layadmin-flexible>a").trigger("click");
    });

    var _index, $body = $("body");
    $body.on("mouseover", "#left-menu-temp.side-shrink li.layui-nav-item", function () {
        _index = layer.tips($(this).find("cite:eq(0)").text(), this);
    });
    $body.on("mouseout", "#left-menu-temp.side-shrink li.layui-nav-item", function () {
        layer.close(_index);
    });

    $body.on("click", "#left-menu-temp.side-shrink li.layui-nav-item", function () {
        $body.removeClass("layadmin-side-shrink");
    });
});

function initLeftMenuCss(hash) {
    if ($("#left-menu-temp").find(".layui-this").length === 0) {
        var $lia = $("li.layui-nav-item>a[href='" + hash + "']");
        var $lidda = $("li.layui-nav-item>dl>dd>a[href='" + hash + "']");
        var $lidddda = $("li.layui-nav-item>dl>dd>dl>dd>a[href='" + hash + "']");
        if ($lia.length > 0) {
            $lia.parents("li.layui-nav-item").addClass("layui-this");
        }
        if ($lidda.length > 0) {
            $lidda.parents("li.layui-nav-item").addClass("layui-nav-itemed");
            $lidda.parent("dd").addClass("layui-this");
        }
        if ($lidddda.length > 0) {
            $lidddda.parent("dd").addClass("layui-this");
            $lidddda.parent("dd").parents("dd").addClass("layui-nav-itemed");
            $lidddda.parents("li.layui-nav-item").addClass("layui-nav-itemed");
        }
    }
}