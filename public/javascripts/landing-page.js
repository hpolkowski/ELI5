/*
 * Obsługa przycisku powrotu do góry strony
 */
const btn = $('#back_to_top_button');

$(window).scroll(function() {
    if ($(window).scrollTop() > 624) {
        btn.addClass('show');
    } else {
        btn.removeClass('show');
    }
});

btn.on('click', function(e) {
    e.preventDefault();
    $('html, body').animate({scrollTop:0}, '300');
});
