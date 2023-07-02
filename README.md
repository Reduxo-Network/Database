# Database


    private Database database;
    private DatabaseCollection mongoCollection;

    @Override
    public void onLoad(){
        this.database = Database.create("adress", 27017, "username", "password", "database");
        this.database.connect();
        this.mongoCollection = database.createDatabaseCollection("players");
    }
    
    @Override
    public void onDisable(){
        this.database.disconnect();
    }
#
    private HazelServer hazelServer;

    @Override
    public void onLoad(){
        this.hazelServer = HazelServer.create("address");
    }

    @Override
    public void onDisable(){
        this.hazelServer.shutdown();
    }
