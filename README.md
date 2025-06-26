# Streque in Clojure
This project is a simple (and unfinished) 3-tier app for paying for things within a choir, along with some minor social media features. The project is primarily used to experiment with software practices and concepts such as CI-pipeline (CD is omitted since the project is not deployed), generative testing and persistent data structures.

## Dependencies
Assuming you have Clojure installed, the remaining dependencies should be automatically installed when running the program. See the file ```deps.edn```.


## Running the dev environment
To launch the dev-environment, run ```clj -X:cljs:dev```.

If you are using Calva, choose cljs and calva-dev. You then need to manually run ```(initialize-backend-environment {})``` in the namespace ```dev```.

The app should be displayed at ```localhost:9500```. The logic layer of the app is at ```localhost:9499``` with end-points such as ```localhost:9499/get-all-users```.

## Building an uber-jar
To build an uber-jar, run ```clj -X:build uber```. The resulting jar should be in the folder ```target```.

## Testing
To run all unit-tests and generative tests, run ```clj -X:test```.