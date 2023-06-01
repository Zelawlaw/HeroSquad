$(document).ready(function() {
    $('.nav-tabs li a').hover(
        function() {
            if (!$(this).hasClass('selected')) {
                $(this).addClass('hover');
            }
        },
        function() {
            $(this).removeClass('hover');
        }
    );


    $('.nav-tabs li a').click(function(e) {

        var href = $(this).attr('href');
   var $tabs = $('.nav-tabs li ');
          $tabs.not(this).removeClass('active');
                  $tabs.removeClass('selected');
                  $(this).parent().addClass('selected').removeClass('hover');
//        if (href !== '#') {
//            // Allow normal behavior for non-empty href
//            return;
//        }
        e.preventDefault();

    });

    // Set the default selected tab
    $('.nav-tabs li.active a').addClass('selected');
});
