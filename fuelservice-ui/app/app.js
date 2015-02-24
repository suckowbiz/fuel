 var app = angular.module('fuel', ['ngAnimate', 'mgcrea.ngStrap', 'mgcrea.ngStrap.tooltip', 'mgcrea.ngStrap.dropdown']);

dropdown = [
  {
    "text": "<i class=\"fa fa-download\"></i>&nbsp;Another action",
    "href": "#anotherAction"
  },
  {
    "text": "<i class=\"fa fa-globe\"></i>&nbsp;Display an alert",
    "click": "$alert(\"Holy guacamole!\")"
  },
  {
    "text": "<i class=\"fa fa-external-link\"></i>&nbsp;External link",
    "href": "/auth/facebook",
    "target": "_self"
  },
  {
    "divider": true
  },
  {
    "text": "Separated link",
    "href": "#separatedLink"
  }
];