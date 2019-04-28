var express = require('express');
var router = express.Router();
const neo4j = require('neo4j-driver').v1;
const neo4jlogin = require('./neo4j.js');

//add neo4j driver and session
const driver = neo4j.driver(neo4jlogin.uri, neo4j.auth.basic(neo4jlogin.user, neo4jlogin.password));
const session = driver.session();

/** POST request for getting friend requests */
router.post("/getfriends", function(req, res, next){
    //get values from query body
    var name = req.body.name;
    var userId = req.body.id;

    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
    'MATCH (a:Person {id: $id})-[:friends]->(b:Person) RETURN b.name, b.id',
    {id: userId}
    );

    //execute promise
    resultPromise.then(result => {
    session.close();
    console.log(result);
    //parse the results
    var sendBack = [];
    for(var i = 0; i < result.records.length;i++ ){
        console.log("In the for loop");
        var singleRecord = result.records[i];
        var name1 = singleRecord.get('b.name');
        var id1 = singleRecord.get('b.id');
        var node = {name: name1, id: id1};
        sendBack.push(node);
    }
    
    //send the results
    res.send(sendBack);

    // on application exit:
    //driver.close();
  })
  .catch(function(rej){
    //log error
    console.log(rej);
  });

});

/** POST request for getting suggested friends*/
router.post("/getsuggestedfriends", function(req, res, next){
  //get values from query body
  var name = req.body.name;
  var userId = req.body.id;

  // create the query to send to neo4j in the form of a promise
  const resultPromise = session.run(
  'Match (a:Person {id:$id}) match (b:Person) where not (b)-[:friends]->(a) and not (b)-[:friendRequest]-(a) return b.name, b.id',
  {id: userId}
  );

  //execute promise
  resultPromise.then(result => {
  session.close();
  console.log(result);
  //parse the results
  var sendBack = [];
  for(var i = 0; i < result.records.length;i++ ){
      console.log("In the for loop");
      var singleRecord = result.records[i];
      var name1 = singleRecord.get('b.name');
      var id1 = singleRecord.get('b.id');
      var node = {name: name1, id: id1};
      sendBack.push(node);
  }
  
  //send the results
  res.send(sendBack);

  // on application exit:
  //driver.close();
})
.catch(function(rej){
  //log error
  console.log(rej);
});

});

/** POST request for getting friend requests */
router.post("/getrequests", function(req, res, next){
    //get values from query body
    var name = req.body.name;
    var userId = req.body.id;

    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
    'MATCH (a:Person {id: $id})<-[:friendRequest]-(b:Person) RETURN b.name, b.id',
    {id: userId}
    );

    //execute promise
    resultPromise.then(result => {
    session.close();
    console.log(result);
    //parse the results
    var sendBack = [];
    for(var i = 0; i < result.records.length;i++ ){
        console.log("In the for loop");
        var singleRecord = result.records[i];
        var name1 = singleRecord.get('b.name');
        var id1 = singleRecord.get('b.id');
        var node = {name: name1, id: id1};
        sendBack.push(node);
    }
    
    //send the results
    res.send(sendBack);

    // on application exit:
    //driver.close();
  })
  .catch(function(rej){
    //log error
    console.log(rej);
  });

});

/** POST request for getting sent friend request */
router.post("/getsent", function(req, res, next){
    //get values from query body
    var name = req.body.name;
    var userId = req.body.id;

    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
    'MATCH (a:Person {id: $id})-[:friendRequest]->(b:Person) RETURN b.name, b.id',
    {id: userId}
    );

    //execute promise
    resultPromise.then(result => {
    session.close();
    console.log(result);
    //parse the results
    var sendBack = [];
    for(var i = 0; i < result.records.length;i++ ){
        console.log("In the for loop");
        var singleRecord = result.records[i];
        var name1 = singleRecord.get('b.name');
        var id1 = singleRecord.get('b.id');
        var node = {name: name1, id: id1};
        sendBack.push(node);
    }
    
    //send the results
    res.send(sendBack);

    // on application exit:
    //driver.close();
  })
  .catch(function(rej){
    //log error
    console.log(rej);
  });

});

/** POST request for getting adding new friends */
router.post("/add", function(req, res, next){
    //get values from query body
    var name1 = req.body[0].name;
    var userId1 = req.body[0].id;
    var name2 = req.body[1].name;
    var userId2 = req.body[1].id;

    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
    'Match (a:Person {id: $id1}) Match (b:Person {id: $id2}) Create (a)-[r:friendRequest]->(b) return r',
    {id1: userId1, id2: userId2}
    );

    //execute promise
    resultPromise.then(result => {
    session.close();
    console.log(result);
    //parse the results
    var sendBack = "Success";
    
    //send the results
    res.send(sendBack);

    // on application exit:
    //driver.close();
  })
  .catch(function(rej){
    //log error and send result
    console.log(rej);
    res.send("Failue");
  });

});

/** POST request for accepting friend requests */
router.post("/accept", function(req, res, next){
    //get values from query body
    var name1 = req.body[0].name;
    var userId1 = req.body[0].id;
    var name2 = req.body[1].name;
    var userId2 = req.body[1].id;

    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
    'match(a:Person{id: $id1})<-[r:friendRequest]-(b:Person{id:$id2}) Create (a)<-[:friends]-(b) create (b)<-[:friends]-(a) delete r',
    {id1: userId1, id2: userId2}
    );

    //execute promise
    resultPromise.then(result => {
    session.close();
    console.log(result);
    //parse the results
    var sendBack = "Success";
    
    //send the results
    res.send(sendBack);

    // on application exit:
    //driver.close();
  })
  .catch(function(rej){
    //log error and send result
    console.log(rej);
    res.send("Failue");
  });

});

/** POST request for denying friend requests */
router.post("/deny", function(req, res, next){
    //get values from query body
    var name1 = req.body[0].name;
    var userId1 = req.body[0].id;
    var name2 = req.body[1].name;
    var userId2 = req.body[1].id;

    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
    'match(a:Person{id: $id1})<-[r:friendRequest]-(b:Person{id:$id2}) Create (a)<-[:notfriends]-(b) create (b)<-[:notfriends]-(a) delete r',
    {id1: userId1, id2: userId2}
    );

    //execute promise
    resultPromise.then(result => {
    session.close();
    console.log(result);
    //parse the results
    var sendBack = "Success";
    
    //send the results
    res.send(sendBack);

    // on application exit:
    //driver.close();
  })
  .catch(function(rej){
    //log error and send result
    console.log(rej);
    res.send("Failue");
  });

});
    

module.exports = router;
