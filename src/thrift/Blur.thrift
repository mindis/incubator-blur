namespace java com.nearinfinity.blur.thrift.generated

exception BlurException {
  1:string message
}

exception MissingShardException {
  1:string message
}

exception EventStoppedExecutionException {
  1:string message
}

enum ScoreType {
  SUPER,
  AGGREGATE,
  BEST,
  CONSTANT
}

struct SearchQuery {
  1:string queryStr,
  2:bool superQueryOn,
  3:ScoreType type, 
  4:string postSuperFilter,
  5:string preSuperFilter,
  6:i64 start,
  7:i32 fetch, 
  8:i64 minimumNumberOfHits,
  9:i64 maxQueryTime,
  10:string user,
  11:i64 userUuid,
  12:i64 systemUuid
}

struct Hit {
  1:string locationId,
  2:double score,
  3:string reason = "UNKNOWN"
}

struct Hits {
  1:i64 totalHits = 0,
  2:map<string,i64> shardInfo,
  3:list<Hit> hits,
  4:list<BlurException> exceptions,
  5:SearchQuery query,
  6:i64 realTime,
  7:i64 cpuTime
}

struct TableDescriptor {
  1:bool isEnabled,
  2:string analyzerDef,
  3:string partitionerClass,
  4:list<string> shardNames
}

struct Column {
  1:string name,
  2:list<string> values
}

struct ColumnFamily {
  1:string family,
  2:map<string,set<Column>> columns
}

struct Row {
  1:string id,
  2:set<ColumnFamily> columnFamilies
}

struct FetchResult {
  1:string table,
  2:string id,
  3:Row row,
  4:bool exists
}

struct Selector {
  1:string id,
  2:string locationId,
  3:list<string> columnFamilies,
  4:map<string,string> columns
}

struct SearchQueryStatus {
  1:SearchQuery query,
  2:i64 realTime,
  3:i64 cpuTime,
  4:double complete
}

service BlurReadOnly {
  list<string> shardServerList() throws (1:BlurException ex)
  list<string> controllerServerList() throws (1:BlurException ex)
  map<string,string> shardServerLayout(1:string table) throws (1:BlurException ex)

  list<string> tableList() throws (1:BlurException ex)
  TableDescriptor describe(1:string table) throws (1:BlurException ex)

  Hits search(1:string table, 2:SearchQuery searchQuery) throws (1:BlurException be, 2: MissingShardException mse, 3: EventStoppedExecutionException esee)
  void cancelSearch(1:i64 userUuid) throws (1:BlurException be, 2: EventStoppedExecutionException esee)
  list<SearchQuery> currentSearches(1:string table) throws (1:BlurException be)
}

service Blur extends BlurReadOnly {
  void batchUpdate(1:string batchId, 2:string table, 3:map<string,string> shardsToUris) throws (1:BlurException be, 2: MissingShardException mse)
  void removeRow(1:string table, 2:string id) throws (1:BlurException be, 2: MissingShardException mse, 3: EventStoppedExecutionException esee)
  void replaceRow(1:string table, 2:Row row) throws (1:BlurException be, 2: MissingShardException mse, 3: EventStoppedExecutionException esee)
  FetchResult fetchRow(1:string table, 2:Selector selector) throws (1:BlurException be, 2: MissingShardException mse, 3: EventStoppedExecutionException esee)
}

service BlurBinary extends Blur {
  void replaceRowBinary(1:string table, 2:string id, 3:binary row) throws (1:BlurException be, 2: MissingShardException mse, 3: EventStoppedExecutionException esee)
  binary fetchRowBinary(1:string table, 2:string id, 3:binary selector) throws (1:BlurException be, 2: MissingShardException mse, 3: EventStoppedExecutionException esee)
}

service BlurAdmin extends BlurBinary {
  void enable(1:string table) throws (1:BlurException ex)
  void disable(1:string table) throws (1:BlurException ex)
  void create(1:string table, 2:TableDescriptor desc) throws (1:BlurException ex)
  void drop(1:string table) throws (1:BlurException ex)

  void shutdownShard(1:string node) throws (1:BlurException be)
  void shutdownController(1:string node) throws (1:BlurException be)
}


