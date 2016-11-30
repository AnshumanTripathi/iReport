/**
 * Created by AnshumanTripathi on 11/29/16.
 */
var mongoose = require("mongoose");
var schema = mongoose.Schema;

var report = new schema({
    "pictures": [schema.objectId],
    "location": {
        lat: Number,
        lng: Number
    },
    "description": {type: String, required: true},
    "size": {type: String, required: true},
    "severity_level": {type: String, required: true},
    "timestamp": {type: Date, required: true, default: Date.now()},
    "userId": {type: schema.ObjectId, required: true},
    "status": {type: String, required: true}
},{
    collection: "report"
});

var Report = mongoose.model('Report', report);

module.exports = Report;