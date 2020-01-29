var exec = require("cordova/exec");

var printer = {
  platforms: ["android"],

  isSupported: function () {
    if (window.device) {
      var platform = window.device.platform;
      if (platform !== undefined && platform !== null) {
        return this.platforms.indexOf(platform.toLowerCase()) >= 0;
      }
    }
    return false;
  },
  printText: function (text, onSuccess, onError, AppId) {
    exec(onSuccess, onError, "VFI", "printText", [text, AppId]);
  },
  printTest: function (text, onSuccess, onError, AppId) {
    exec(onSuccess, onError, "VFI", "printTest", [text, AppId]);
  },
  printJson: function (text, onSuccess, onError, AppId) {
    exec(onSuccess, onError, "VFI", "printJson", [text, AppId]);
  },
  printBase64: function (base64, onSuccess, onError) {
    exec(onSuccess, onError, "VFI", "printBase64", [base64]);
  },
  printPath: function (base64, onSuccess, onError) {
    exec(onSuccess, onError, "VFI", "printPath", [base64]);
  }
};
module.exports = printer;