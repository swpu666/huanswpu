$(document).ready(function() {
    $('.navA').click(function() {
        $(this).toggleClass('on');
        if ($(this).hasClass('on')) {
            $(".g-nav2").fadeIn();
        } else {
            $(".g-nav2").fadeOut();
        }
    });
    $('.g-nav2').find('li').each(function() {
        var li = $(this);
        if (li.find('.list').length > 0) {
            li.find('h2').addClass('h2');
            li.find('h2').click(function() {
                if ($(window).width() > 1200) return;
                if (li.hasClass('on')) {
                    li.removeClass('on');
                    li.find('.list').hide()
                } else {
                    li.addClass('on');
                    li.find('.list').show()
                }
            })
        }
    });
});