var express = require('express');
var app = express();
var bodyParser =  require("body-parser");
var fs = require('fs');
var mysql  = require('mysql');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  port: '3306',
  database: 'mobile',
});
connection.connect();
app.post('/sign', function (req,res) {
    var param=req.body.phone
    console.log('sign')
    var searchSql = 'select userid from user where phone=?';
    var addSql = 'insert into user (phone)values(?) ;'
    var result;
    console.log('sign')
    connection.query(searchSql,param,function (err,results,fields) {
        if(err){
             console.log('[add ERROR] - ',err.message)
        }
        if(results.length>0){
            console.log(results[0])
            res.send(results[0]);
        }
        else{
                connection.query(addSql,param,function (err,results,fields) {
                        if(err){
                         console.log('[add ERROR] - ',err.message)
                                                 }
                        console.log("add")
                        res.send(results);
                        console.log("add")
                })

        }
  })

})

app.post('/backup',function (req, res) {
   console.log("backup");
   var user_id=req.body.user_id;
   console.log(req.body)
   fs.writeFileSync("backend/"+user_id+".txt",JSON.stringify(req.body));
   var data={"su":"su"};
   var re=JSON.stringify(data);
   res.send(re);

})

app.post('/down', function (req, res) {
   console.log('down')
   var user_id=req.body.id;
   console.log(req.body);
   console.log(user_id);
   var data = fs.readFileSync("backend/"+user_id+".txt", 'utf8');
   console.log(data);
   var re=JSON.parse(data);
   console.log(re);
   res.send(re);
})
            
