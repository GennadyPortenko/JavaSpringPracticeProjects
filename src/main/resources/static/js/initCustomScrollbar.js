function initCustomScrollbar(container) {
  var $ = document.querySelector.bind(document);

    var ps = new PerfectScrollbar(container);

    function updateSize() {
      var width = parseInt($('#width').value, 10);
      var height = parseInt($('#height').value, 10);

      $(container).style.width = width + 'px';
      $(container).style.height = height + 'px';

      ps.update();
  }
}