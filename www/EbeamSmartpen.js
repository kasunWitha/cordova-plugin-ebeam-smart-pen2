var exec = require('cordova/exec');

function EbeamSmartpen() {}


EbeamSmartpen.prototype.create = function(success, error){
    exec(success, error, 'EbeamSmartpen', 'create');
};

EbeamSmartpen.prototype.isPenMode = function(success, error){
    exec(success, error, 'EbeamSmartpen', 'isPenMode');
};

EbeamSmartpen.prototype.connect = function(arg0, success, error) {
    exec(success, error, 'EbeamSmartpen', 'connect', [arg0]);
};

EbeamSmartpen.prototype.onPenEvent = function( callback, success, error ){
    EbeamSmartpen.prototype.onEventReceived = callback;
    exec(success, error, "EbeamSmartpen", "start", []);
};

EbeamSmartpen.prototype.onEventReceived = function(payload){
    console.log("Pen event received");
};

EbeamSmartpen.prototype.onPenMessage = function( callback, success, error ){
    EbeamSmartpen.prototype.onMessageReceived = callback;
    exec(success, error, "EbeamSmartpen", "start", []);
};

EbeamSmartpen.prototype.onMessageReceived = function(payload){
    console.log("Pen message received");
};

var ebeamSmartpen = new EbeamSmartpen();
module.exports = ebeamSmartpen;

EbeamSmartpen.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }
    window.plugins.EbeamSmartpen = new EbeamSmartpen();
    return window.plugins.EbeamSmartpen;
};
cordova.addConstructor(EbeamSmartpen.install);