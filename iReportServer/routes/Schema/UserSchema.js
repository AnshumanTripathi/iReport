/**
 * Created by AnshumanTripathi on 11/29/16.
 */
var mongoose = require("mongoose");
var schema = mongoose.Schema;

var user = new schema({
    "email": {type: String, required: true, unique: true},
    "first_name": {type: String, required: true},
    "last_name": {type: String, required: true},
    "home_address": {type: String},
    "screen_name": {type: String, required: true, unique: true},
    "isOfficial": {type: Boolean},
    "settings": {
        "email_confirm": Boolean,
        "email_notify": Boolean,
        "anonymous": Boolean
    }
}, {
    collection: "user"
});

var User = mongoose.model('User', user);


module.exports = User;