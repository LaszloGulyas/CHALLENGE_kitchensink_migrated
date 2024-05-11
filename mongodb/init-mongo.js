// This init script is used by docker-compose during container startup

// Create user
db = db.getSiblingDB('kitchensink_migrated_db');
db.createUser({
    user: "test",
    pwd: "test1234",
    roles: [{role: "readWrite", db: "kitchensink_migrated_db"}]
});

// Add member table and init record
db = db.getSiblingDB('kitchensink_migrated_db');
db.createCollection("member");
db.getCollection("member").insertOne({
    "_id": ObjectId("663f504e9388508823166a61"),
    "name": "John Smith",
    "email": "john.smith@mailinator.com",
    "phone_number": "2125551212"
});
