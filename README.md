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
    public void disable(){
        this.database.disconnect();
    }
#
    private HazelServer hazelServer;

    @Override
    public void onLoad(){
        this.hazelServer = HazelServer.create("address");
    }

    @Override
    public void disable(){
        this.hazelServer.shutdown();
    }
