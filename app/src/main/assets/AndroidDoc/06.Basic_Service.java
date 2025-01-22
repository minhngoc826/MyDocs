// 6.Service.java

Here it goes,

Its only a directional tag indicating which way the data goes.
in - object is transferred from client to service only used for inputs
out - object is transferred from client to service only used for outputs.
inout - object is transferred from client to service used for both inputs and outputs.
All non-primitive parameters require a directional tag indicating which way the data goes. Either in, out, or inout.

Primitives are in by default, and cannot be otherwise

Please note, RPC calls from clients are synchronous.
You should limit the direction to what is truly needed, because marshaling parameters is expensive.
Example: Please check the below AIDL interface to understand it in a better way.

package com.hardian.sample.aidl;
import com.hardian.sample.aidl.TeamMember;

interface ITeamManageService {
void getTeamCaptian(out TeamMember member);
void updateTeamMember(inout TeamMember member, in boolean isLeader);
oneway void removeTeamMember(in TeamMember member);
}
Here we have used out, in, inout directional tags to indicate which way the data goes.

getTeamCaptian(out TeamMember member) : Get the captain of the team. Here the "out" directional tag means, when the client calls this method, the "member" object has no relevant data, but the server shall make changes to the "member" object, so the client shall get the updated "member" object. In fact, the method call is synchronous.

updateTeamMember(inout TeamMember member, in boolean isLeader) : Update the captian of the team. Here the "inout" directional tag means, when the client calls this method,the "member" object has relevant data in it. And the server shall use the input data and process it. Once the process completed, the client shall get the relevant data back. In fact, the method call is synchronous.

removeTeamMember(in TeamMember member) Remove a member from the team. Here the "in" directional tag means, the "member" object is transferred from client to service only used for inputs. If any changes are made to the "member" object in the service then it won’t reflect in the client. The method call is asynchronous, we can put the "oneway" keyword to the method signature. Asynchronous methods must not have "out" and "inout" arguments, they must also return void.
