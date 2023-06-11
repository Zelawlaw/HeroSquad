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
        if (href !== '#') {
            // Allow normal behavior for non-empty href
            return;
        }
        e.preventDefault();

    });

    // Set the default selected tab
    $('.nav-tabs li.active a').addClass('selected');
});


function toggleSquadForm() {
    var squadForm = document.getElementById("squadForm");
    var showSquadCheckbox = document.getElementById("showSquad");

    if (showSquadCheckbox.checked) {
        squadForm.style.display = "block";
    } else {
        squadForm.style.display = "none";
    }
}


// Define the ifEquals helper function
Handlebars.registerHelper('ifItsEqual', function(arg1, arg2, options) {
  return (arg1 == arg2) ? options.fn(this) : options.inverse(this);
});
