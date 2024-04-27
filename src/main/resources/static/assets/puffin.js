function bindModel(form, data) {
  const fields = form.querySelectorAll('[name]:not([name=""])');
  for (const field of fields) {
    const { tagName, type, name } = field;

    if (tagName === 'TH-FIELD' || tagName === 'TH-CALENDAR' || (tagName === 'INPUT' && type === 'hidden')) {
      field.value = data[name] || '';
    } else if (tagName === 'SELECT' || tagName === 'TH-SELECT') {
        console.log(data[name]);
      field.value = data[name];
    } else if (type === 'checkbox') {
      field.checked = data[name];
    }
  }
}