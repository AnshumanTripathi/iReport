/**
 * Created by AnshumanTripathi on 11/29/16.
 */
var mongoose = require("mongoose");
var schema = mongoose.Schema;

var report = new schema({
    "pictures": {type: String, required: false},
    "location": {
        lat: Number,
        lng: Number
    },
    "description": {type: String, required: true},
    "size": {type: String, required: true},
    "severity_level": {type: String, required: true},
    "timestamp": {type: Date},
    "user_email": {type: String},
    "user_screen_name": {type: String},
    "status": {type: String, required: true},
    "isAnonymous": {type: Boolean}
},{
    collection: "report"
});

report.pre('save',function (next) {
    if(this.timestamp == null) {
        this.timestamp = Date.now();
    }
    next();
});
var Report = mongoose.model('Report', report);

module.exports = Report;