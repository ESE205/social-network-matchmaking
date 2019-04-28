var express = require('express');
var router = express.Router();
const neo4j = require('neo4j-driver').v1;
const neo4jlogin = require('./neo4j.js');

//add neo4j driver and session
const driver = neo4j.driver(neo4jlogin.uri, neo4j.auth.basic(neo4jlogin.user, neo4jlogin.password));
const session = driver.session();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

/** POST request for creating new user */
router.post('/create', function(req, res, next) {

  //get values from query body
  var name = req.body.name;
  var userId = req.body.id;
  var gender = req.body.gender;
  console.log(gender);
  // create the query to send to neo4j in the form of a promise
  const resultPromise = session.run(
    'CREATE (a:Person:'+gender+' {id: $id, name: $name}) RETURN a',
    {id: userId, name: name, gender: gender}
  );

  //execute promise
  resultPromise.then(result => {
    session.close();

    //parse the results
    const singleRecord = result.records[0];
    const node = singleRecord.get(0);

    //send the results
    var sendBack = {user: node.properties}
    res.send({"name": node.properties.name, "id": node.properties.id});

    // on application exit:
    driver.close();
  })
  .catch(function(rej){
    //log error
    console.log(rej);
  });

});

module.exports = router;
