A tentative reproducer for https://github.com/apollographql/apollo-android/issues/2998

Run `./gradlew test -i` to test.

Expected logs:
```
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: --> GET https://apollo-fullstack-tutorial.herokuapp.com/graphql?operationName=GetLaunches&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2212893c9eb85d07fd1e4fe8270a207846eb52fbc30a20ec996b5628c0f213e2fa%22%7D%7D
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: Accept: application/json
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-OPERATION-ID: 12893c9eb85d07fd1e4fe8270a207846eb52fbc30a20ec996b5628c0f213e2fa
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-OPERATION-NAME: GetLaunches
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-CACHE-KEY: 44b5a1a4e83be811e3ffd1de4512ffd1
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-CACHE-FETCH-STRATEGY: NETWORK_FIRST
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-EXPIRE-TIMEOUT: 0
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-EXPIRE-AFTER-READ: false
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-PREFETCH: false
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-CACHE-DO-NOT-STORE: false
Mar 13, 2021 5:07:46 PM okhttp3.internal.platform.Platform log
INFO: --> END GET
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: <-- 200 OK https://apollo-fullstack-tutorial.herokuapp.com/graphql?operationName=GetLaunches&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2212893c9eb85d07fd1e4fe8270a207846eb52fbc30a20ec996b5628c0f213e2fa%22%7D%7D (672ms)
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Server: Cowboy
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Connection: keep-alive
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Access-Control-Allow-Origin: *
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Content-Type: application/json; charset=utf-8
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Cache-Control: private, no-cache, must-revalidate
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Content-Length: 100
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Etag: W/"64-pCNUOD6DZiPor9gRPcTPDcQA2zU"
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Date: Sat, 13 Mar 2021 16:07:47 GMT
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Via: 1.1 vegur
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-SERVED-DATE: Sat, 13 Mar 2021 16:07:47 GMT
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: 
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: {"errors":[{"message":"PersistedQueryNotFound","extensions":{"code":"PERSISTED_QUERY_NOT_FOUND"}}]}

Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: <-- END HTTP (100-byte body)
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: --> GET https://apollo-fullstack-tutorial.herokuapp.com/graphql?query=query%20GetLaunches%20%7B%20launches%20%7B%20__typename%20launches%20%7B%20__typename%20id%20%7D%20%7D%20%7D&operationName=GetLaunches&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2212893c9eb85d07fd1e4fe8270a207846eb52fbc30a20ec996b5628c0f213e2fa%22%7D%7D
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: Accept: application/json
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-OPERATION-ID: 12893c9eb85d07fd1e4fe8270a207846eb52fbc30a20ec996b5628c0f213e2fa
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-OPERATION-NAME: GetLaunches
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-CACHE-KEY: 44b5a1a4e83be811e3ffd1de4512ffd1
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-CACHE-FETCH-STRATEGY: NETWORK_FIRST
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-EXPIRE-TIMEOUT: 0
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-EXPIRE-AFTER-READ: false
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-PREFETCH: false
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-CACHE-DO-NOT-STORE: false
Mar 13, 2021 5:07:47 PM okhttp3.internal.platform.Platform log
INFO: --> END GET
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: <-- 200 OK https://apollo-fullstack-tutorial.herokuapp.com/graphql?query=query%20GetLaunches%20%7B%20launches%20%7B%20__typename%20launches%20%7B%20__typename%20id%20%7D%20%7D%20%7D&operationName=GetLaunches&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2212893c9eb85d07fd1e4fe8270a207846eb52fbc30a20ec996b5628c0f213e2fa%22%7D%7D (4880ms)
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Server: Cowboy
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Connection: keep-alive
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Access-Control-Allow-Origin: *
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Content-Type: application/json; charset=utf-8
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Content-Length: 759
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Etag: W/"2f7-RHatURM+8nuuLCHlPTNhA2ni8/w"
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Date: Sat, 13 Mar 2021 16:07:51 GMT
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: Via: 1.1 vegur
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: X-APOLLO-SERVED-DATE: Sat, 13 Mar 2021 16:07:52 GMT
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: 
Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: {"data":{"launches":{"__typename":"LaunchConnection","launches":[{"__typename":"Launch","id":"109"},{"__typename":"Launch","id":"108"},{"__typename":"Launch","id":"107"},{"__typename":"Launch","id":"106"},{"__typename":"Launch","id":"105"},{"__typename":"Launch","id":"104"},{"__typename":"Launch","id":"103"},{"__typename":"Launch","id":"102"},{"__typename":"Launch","id":"101"},{"__typename":"Launch","id":"100"},{"__typename":"Launch","id":"99"},{"__typename":"Launch","id":"98"},{"__typename":"Launch","id":"97"},{"__typename":"Launch","id":"96"},{"__typename":"Launch","id":"95"},{"__typename":"Launch","id":"94"},{"__typename":"Launch","id":"93"},{"__typename":"Launch","id":"92"},{"__typename":"Launch","id":"91"},{"__typename":"Launch","id":"90"}]}}}

Mar 13, 2021 5:07:52 PM okhttp3.internal.platform.Platform log
INFO: <-- END HTTP (759-byte body)
Response(operation=com.library.GetLaunchesQuery@36f1ff71, data=Data(launches=Launches(__typename=LaunchConnection, launches=[Launch(__typename=Launch, id=109), Launch(__typename=Launch, id=108), Launch(__typename=Launch, id=107), Launch(__typename=Launch, id=106), Launch(__typename=Launch, id=105), Launch(__typename=Launch, id=104), Launch(__typename=Launch, id=103), Launch(__typename=Launch, id=102), Launch(__typename=Launch, id=101), Launch(__typename=Launch, id=100), Launch(__typename=Launch, id=99), Launch(__typename=Launch, id=98), Launch(__typename=Launch, id=97), Launch(__typename=Launch, id=96), Launch(__typename=Launch, id=95), Launch(__typename=Launch, id=94), Launch(__typename=Launch, id=93), Launch(__typename=Launch, id=92), Launch(__typename=Launch, id=91), Launch(__typename=Launch, id=90)])), errors=null, dependentKeys=[], isFromCache=false, extensions={}, executionContext=com.apollographql.apollo.http.OkHttpExecutionContext@1f3e95d4)
BUILD SUCCESSFUL in 13s
7 actionable tasks: 3 executed, 4 up-to-date
17:07:52: Task execution finished ':test --tests "MainTest.test"'.

```
