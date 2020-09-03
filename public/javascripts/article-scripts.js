/**
 * Zmienia akcję formularza na podgląd, wykonuje go potem odwraca zmiany
 *
 * @param element przycisk wykonujący żądanie
 * @param route   nowa ścieżka
 */
function articlePreview(element, route) {
    event.preventDefault();

    const form = jQuery(element).closest('form');
    const action = form.attr('action');

    form.attr('action', route);
    form.attr('target', '_blank');

    form.submit();

    form.attr('action', action);
    form.attr('target', '');
}
