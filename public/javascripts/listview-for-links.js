/**
  * Oblicza informacje o rozmiarze listy na stronie po wyborze pola select i określeniu wielkości listy w danym oknie
  */
function showListRegardPageSize()
{
	window.location.href = jQuery("select[name='pageSize']").find('option:selected').val();
}

/**
  * Przekeirowuje po kliknięciu w nagłówek sortujący
  */
function showListRegardOrdering(href)
{
	window.location.href = href;
}

/**
  * Przekeirowuje do listy z zaaplikowanymi filtrami
  *
  * @param form formularz wykonujący polecenie
  */
function showListRegardFilter(form)
{
	var addon = "?f=";

	if(form.target.includes("?")) addon = "&f=";
	window.location.href = form.target + addon + jQuery("input[name='filter']").val();
}
