function bindModel(form, data) {
  const fields = form.querySelectorAll('[name]:not([name=""])');
  for (const field of fields) {
    const { tagName, type, name } = field;

    if (tagName === 'TH-FIELD' || (tagName === 'INPUT' && type === 'hidden')) {
      field.value = data[name] || '';
    } else if (tagName === 'SELECT') {
      field.value = data[name].code;
    } else if (type === 'checkbox') {
      field.checked = data[name];
    }
  }
}