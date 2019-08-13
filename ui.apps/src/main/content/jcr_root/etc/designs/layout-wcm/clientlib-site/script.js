/** a JS file that shall be included */

$(document).ready(function(){

    var $flyouts = $('.nav-flyout');
    var currentlyActivePath = '';

    $(document).click(function(event){
        var $flyout = $(event.target).closest(".nav-flyout.active,.flyout-link");
        if($flyout.length === 0){
            $flyouts.removeClass('active');
            currentlyActivePath = '';
        }
    });

    $('.flyout-link').click(function(event){

        event.preventDefault();

        var targetPath = $(this).attr('href');
        $flyouts.removeClass('active');

        if(currentlyActivePath != targetPath){
            $flyouts.filter('[data-flyout-path="' + targetPath + '"]').addClass('active');
            currentlyActivePath = targetPath;
        }else{
            currentlyActivePath = '';
        }
    });


});