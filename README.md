# Features
The reactive data pipe is a logical channel of continuous data processing, that can dynamically scale up/out and shrink back 
according to the incoming data influx, and hence react to variations in the incoming load.

The application is built in scala, and relies on the Akka implementation to the Actor model, in addition to few other Akka modules like Akka-Remote, Akka-Cluster, Akka-Persistence and the experimental Akka-Stream. 

It starts with a data source, that can be connected to any source of data stream, twitter feed could be an example. It ends with a data sink, which consumes the outcoming processed data and pushes it to a configurable set of destinations.

Each Sink corresponds to a single destination. Several Sinks could be simultaneously connected to the same data pipe. The data will then translated, transformed and processed once and published to several places

Data Processing is carried out by a set of Spark jobs, integrated with the Processing Engine (pipe-processor)

Each data element passing through the pipe, is called [Data Pill], and hence the pipe is called a pipe of data pills
The pipe construction is designed to be lazy, meaning that it doesn't ignited until a Sink is connected to it.

The following modules represnt the main constituents of the Reactive Data Pipe.
* #### Pipe_Source
  1. Acts as the source of data for the entire pipe. Can be though of as the driver of the pipe.
  2.

* #### Pipe_Transformer 
  1. This is where the content of an incoming Data Pill, gets transformed from one format to another, or one structure to      another. Example: A DataPill[String] gets transformed to DataPill[Tweet], which can then be passed to a successive processing task.
  
* #### Pipe_Processor
  1. This is the place where real processing and data manipulation takes place. 
  2. Acts as the source of information, extracted from a continous stream that came in
  3. Complex processing is carried out by Spark

* #### Pipe_Sink
  1. Acts as the final sink, into which a stream of processed data pills will be caught.

# Usage

* To run the system, you will need to run each component separaetly (this needs to be changed, and a single script should be able to trigger all components)
* The entry points for the different components are: 
* [pipe_source] -> na.datapipe.source.engine.ConsumerEngine
* [pipe_processor] -> na.datapipe.process.ProcessorEngine
* [pipe_transformer] -> na.datapipe.transformer.initator.TransformerEngine
* [pipe_sink] -> na.datapipe.sink.initiator.Sinkengine
* The order in which, the different components are started, is not relevant. As soon as a component is started, it will attempt to join the cluster. Once joined, every other compoment already in the cluster will be notified and the cluster member list will be modified few seconds later.

* To be able to view the running spark jobs, their execution perfomance, go to: http://10.140.95.31:4040/jobs/

### TO DO List:

### Unit Test Cases
1. ScalaTest
2. akka.testkit

#### Introduce Akka-Persistence
1. Enable Reliable Messaging Between the different distributed components.
2. Should be able to re-run the list of events that occured in the past, only from a predefined snapshot 

#### Integtrate a NoSQL DB.
1. To persist accumulative intermediate results during spark job processing.
2. MongoDB, could be used to store the processed Pills in Json Documents
3. Rely on a Reactive, asynchronous access to a persistent storage, ReactiveMongo is a good candidate.

#### Introduce a DSL like syntax
1. Introduce a DSL grammar where the whole chain can be programtically described in one line ``` TextPipe("bla bla bla").filter(filterFunc: Pill => Boolean).transform(transformFunc : Pill[A] => Pill[B])((implicit evidence: A=>B)).translate().push(destination: Pill => Sink).materialize ``` 

#### Introduce Akka Routing strategies
1. Stop relying on trivial random routing logic, similar to the one used in the pipe-transformer module ```processingEngines(jobCounter % processingEngines size) ! ProcessPill(pill) ``` 
