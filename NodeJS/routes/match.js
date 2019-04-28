var express = require('express');
var router = express.Router();
const neo4j = require('neo4j-driver').v1;
const neo4jlogin = require('./neo4j.js');

//add neo4j driver and session
const driver = neo4j.driver(neo4jlogin.uri, neo4j.auth.basic(neo4jlogin.user, neo4jlogin.password));
const session = driver.session();

/* POST for getting queue of matches */
router.post('/getqueue', function(req, res, next) {
  
  //get values from query body
  var name = req.body.name;
  var userId = req.body.id;

  // create the query to send to neo4j in the form of a promise
  const resultPromise = session.run(
    'match (a:Female)-[:friends]-> (b:Person{id:$id})<-[:friends]-(c:Male) where not (b)-[:endorse]->(:match{fid:a.id, mid:c.id}) and not (:match{fid:a.id, mid:c.id})-[]->(:matchComplete) merge (m:match{fname:a.name, fid:a.id, mname:c.name, mid: c.id}) with m as ma return ma.fid as id1, ma.fname as name1, ma.mid as id2, ma.mname as name2, size((ma)<--()) as mCount order by mCount limit 10',
    {id: userId, name: name}
  );

  //execute promise
  resultPromise.then(result => {
    session.close();

    //parse the results
    var sendBack = [];
    console.log("starting for loop");
    for(var i = 0; i < result.records.length;i++ ){
        console.log("In the for loop");
        var singleRecord = result.records[i];
        var name1 = singleRecord.get('name1');
        var id1 = singleRecord.get('id1');
        var name2 = singleRecord.get('name2');
        var id2 = singleRecord.get('id2');
        var node = {name1: name1, userid1: id1, name2: name2, userid2:id2};
        sendBack.push(node);
    }

    //send the results
    console.log("sending back results");
    res.send(sendBack);

    // on application exit:
    // console.log("closing the application");
    // driver.close();
  })
  .catch(function(rej){
    //log error
    console.log(rej);
  });
});

/** POST request for getting list of matches */
router.post('/getlist', function(req, res, next) {

  //get values from query body
  var name = req.body.name;
  var userId = req.body.id;

  // create the query to send to neo4j in the form of a promise
  const resultPromise = session.run(
    'match (a{id:$id})-[r:matchedWith]->(m:match)<-[t:matchedWith]-(b) where not (m)-[]-(:matchComplete) return r.status as status1,t.status as status2, b.name as name, b.id as id',
    {id: userId, name: name}
  );

  //execute promise
  resultPromise.then(result => {
    console.log("closing the session");
    session.close();

    //parse the results
    var sendBack = [];
    console.log("starting the for loop");
    for(var i = 0; i < result.records.length;i++ ){
        console.log("In the for loop");
        var singleRecord = result.records[i];
        var name1 = name;
        var id1 = userId;
        var name2 = singleRecord.get('name');
        var id2 = singleRecord.get('id');
        var status1 = singleRecord.get('status1');
        var status2 = singleRecord.get('status2');
        if(status1==null){
            status1 = " ";
        }

        if(status2==null){
            status2=" ";
        }
        var node = {name1: name1, userid1: id1, status1:status1, name2: name2, userid2:id2, status2:status2, endorsers: 0};
        sendBack.push(node);
    }

    //send the results
    console.log("sending back results");
    res.send(sendBack);

    // on application exit:
    // console.log("closing the driver");
    // driver.close();
  })
  .catch(function(rej){
    //log error
    console.log(rej);
  });

});

  /** POST request for endorsing a match */
router.post('/endorseMatchInfo', function(req, res, next) {
    console.log(req.body);
    //get values from query body
    var name = req.body.name1;
    var userId = req.body.userid1;
    var fname = req.body.name2;
    var mname = req.body.name3;
    var fid = req.body.userid2;
    var mid = req.body.userid3;
  
    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
      'match (m:match{fid:$fid,mid:$mid}) match (f:Person{id:$fid}) match (x:Person{id:$mid}) match (i:Person{id:$id}) merge (i)-[:endorse{type:"yes"}]->(m) merge (f)-[r:matchedWith]->(m)<-[v:matchedWith]-(x)',
      {id: userId, name: name, fname:fname, mname: mname, fid:fid, mid:mid}
    );
  
    //execute promise
    resultPromise.then(result => {
      session.close();
  
      //send the results
      res.send("success");
  
      // on application exit:
      //driver.close();
    })
    .catch(function(rej){
      //log error
      console.log(rej);
    });
  
  });

    /** POST request for endorsing a match */
router.post('/denyMatchInfo', function(req, res, next) {
    console.log(req.body);
    //get values from query body
    var name = req.body.name1;
    var userId = req.body.userid1;
    var fname = req.body.name2;
    var mname = req.body.name3;
    var fid = req.body.userid2;
    var mid = req.body.userid3;
  
    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
      'match (m:match{fid:$fid,mid:$mid}) match (f:Person{id:$fid}) match (x:Person{id:$mid}) match (i:Person{id:$id}) merge (i)-[:endorse{type:"no"}]->(m)',
      {id: userId, name: name, fname:fname, mname: mname, fid:fid, mid:mid}
    );
  
    //execute promise
    resultPromise.then(result => {
      session.close();
  
      //send the results
      res.send("success");
  
      // on application exit:
      //driver.close();
    })
    .catch(function(rej){
      //log error
      console.log(rej);
    });
  
  });

      /** POST request for endorsing a match */
router.post('/acceptMatch', function(req, res, next) {
    console.log(req.body);
    //get values from query body
    var name = req.body.name1;
    var userId = req.body.userid1;
    var name2 = req.body.name2;
    var id2 = req.body.userid2;
    
  
    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
      'match (a:Person{id:$id1}) match (b:Person{id:$id2}) match (a)-[r1:matchedWith]->(m)<-[r2:matchedWith]-(b) set r1.status="yes"',
      {id1: userId, id2:id2}
    );
  
    //execute promise
    resultPromise.then(result => {
      session.close();
  
      //send the results
      res.send("success");
  
      // on application exit:
      //driver.close();
    })
    .catch(function(rej){
      //log error
      console.log(rej);
    });
  
  });

  router.post('/denyMatch', function(req, res, next) {
    console.log(req.body);
    //get values from query body
    var name = req.body.name1;
    var userId = req.body.userid1;
    var name2 = req.body.name2;
    var id2 = req.body.userid2;
    
  
    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
      'match (a:Person{id:$id1}) match (b:Person{id:$id2}) match (a)-[r1:matchedWith]->(m)<-[r2:matchedWith]-(b) set r1.status="no" create (m)-[:matchFailure]->(:matchComplete)',
      {id1: userId, id2:id2}
    );
  
    //execute promise
    resultPromise.then(result => {
      session.close();
  
      //send the results
      res.send("success");
  
      // on application exit:
      //driver.close();
    })
    .catch(function(rej){
      //log error
      console.log(rej);
    });
  
  });

  router.post('/completeAcceptMatch', function(req, res, next) {
    console.log(req.body);
    //get values from query body
    var name = req.body.name1;
    var userId = req.body.userid1;
    var name2 = req.body.name2;
    var id2 = req.body.userid2;
    
  
    // create the query to send to neo4j in the form of a promise
    const resultPromise = session.run(
      'match (a:Person{id:$id1}) match (b:Person{id:$id2}) match (a)-[r1:matchedWith]->(m)<-[r2:matchedWith]-(b) set r1.status="yes" create (m)-[:matchSuccess]->(:matchComplete)',
      {id1: userId, id2:id2}
    );
  
    //execute promise
    resultPromise.then(result => {
      session.close();
  
      //send the results
      res.send("success");
  
      // on application exit:
      //driver.close();
    })
    .catch(function(rej){
      //log error
      console.log(rej);
    });
  
  });

module.exports = router;