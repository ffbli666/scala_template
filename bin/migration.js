var db = connect('localhost/management');
db.createCollection("user");
db.user.insert({
    "firstname" : "XiaoMing",
    "lastname"  : "Wang",
    "fullname"  : "XiaoMing Wang",
    "email"     : "abc@tw.yahoo.com",
    "gender"    : "男"
});

db.user.insert({
    "firstname" : "小明",
    "lastname"  : "王",
    "fullname"  : "王小明",
    "email"     : "bbb@tw.yahoo.com",
    "gender"    : "男"
});