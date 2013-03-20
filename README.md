RoboFuehrer
===========

The LHS submission for the computer programming event of the Pummill Math Relays.

The robot implements a variety of algorithms for movement and targeting. In group situations, it uses a finely-tuned 
version of minimum risk movement, which itself is a generalization of anti-gravity movement. In one-on-one movement,
it uses a combination of pseudo-random orbital movement and the so-called "Musashi trick."

For targeting, the robot runs a simulation in the background of all available algorithms to figure out which ones
are most effective against a particular target. Among available targeting strategies are simple linear and
circular predictive models as well as much more complex statistical algorithms like guess factor targeting.
