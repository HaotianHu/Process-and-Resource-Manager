# Process-and-Resource-Manager
Using JAVA to simulate the process and resource manager in operating system.



Pseudo code for the Process and Resource Manager using multiunit resources.
i.state is the state of process i
i.resources is a list of pairs (r, k) where r is a resource and k is the number of units that process i is holding
r.state is a counter that keeps track of the currently available units of r
r.waitlist contains pairs (i, k) where i is the waiting process and k is the number of requested units
Note that a release of k units may enable more than one process 
from r.waitlistrequest(r, k){   if (r.state >= k)      r.state = r.state - k      insert (r, k) into i.resources   else      i.state = blocked      remove i from RL      insert (i, k) into r.waitlist      scheduler()}
release(r){   remove (r, k) from i.resources   r.state = r.state + k   while (r.waitlist != empty and r.state > 0)      get next (j, k) from r.waitlist      if (r.state >= k)         r.state = r.state - k         insert (r, k) into j.resources         j.state = ready                   remove (j, k) from r.waitlist         insert j into RL      else break   scheduler()}



Project Specifications: Data Structures and Functions•Design and implement the extended version of the manager, including: •PCB, RCB, and RL data structures•functions create(), destroy(), request(), release(), timeout(), scheduler(), init()•Design and implement the shell (see command language and output specifications)•Instantiate manager to include the following at start-up: •A process descriptor array PCB[16]•A resource descriptor array RCB[4] with multiunit resources•RCB[0] and RCB[1] have 1 unit each; RCB[2] has 2 units; RCB[3] has 3 units•A 3-level RL•The init function should always perform the following tasks:•Erase all previous contents of the data structures PCB, RCB, RL•Create a single running process at PCB[0] with priority 0•Enter the process into the RL at the lowest-priority level 0

