# RPC-HTTP
My implemention of a RPC framework use HTTP in 2015.

### Core ###
`xdean.rpc.main.HttpRpcConsumer` and `xdean.rpc.main.HttpRpcRegister`
### Test ###
1. run the test on the server (I'm too lazy, so it depends on J2EE)
2. run test cases in `xdean.rpc.test.testcase`
#### Known Bugs ####
1. Don't support block(long time operator) because HTTP timeout...
2. Don't share system IO