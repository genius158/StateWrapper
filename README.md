# StateWrapper 
#
![demo gif](demo.gif)
## 1.notice
StateWrapper is used to set the state like no data , no net and so on ,
 I coded a adapter to set different data type for recyclerview before ,it could also
 do the same thing like StateWrapper , but to do that I have to do some operations useless
 and the extending is not good
<br/>
StateWrapper , when you call the method setShowStateView(true) , the state view that you
 have set will come out , do not to modify the data source 
<br/>
 

## 2.use  
```
    // control the state view show or not
   stateWrapper.setShowStateView(isShow)
```
