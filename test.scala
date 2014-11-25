object SomeoneElsesLibrary {
  object ResponseTypeClass {
    trait TheirResponse[A] {
      def serialized(a : A) : String;
    }

    implicit object StringTheirResponse extends TheirResponse[String] {
      def serialized(string : String) = string; 
    }

    implicit object IntTheirResponse extends TheirResponse[Int] {
      def serialized(int : Int) = int.toString; 
    }
  }

  class Service {
    import ResponseTypeClass._
    def send[A : TheirResponse](a : A) : Unit = {
      println(implicitly[TheirResponse[A]].serialized(a));
    }
  }
}

object MyClient {

  trait Sendable[T]
  implicit object SendableString extends Sendable[String]
  implicit object SendableInt extends Sendable[Int]

  trait Sender[T] {
    def sendWith[A : Sendable](service : T, x : A) : Unit
  }

  object Adapters {
    import SomeoneElsesLibrary.Service
    implicit object SomeoneElsesServiceSender extends Sender[Service] {
      def sendWith[A : Sendable](service : Service,  x : A) : Unit = service.send(x);
    } 
  } 

  class Client[T : Sender](service : T) {
    def send[A : Sendable](x : A) : Unit = implicitly[Sender[T]].sendWith(service, x) 
  }
}

object Test extends App {
  import MyClient._
  import MyClient.Adapters._
  import SomeoneElsesLibrary.Service
  val service = new Service
  val client = new Client(service)
  client.send("Hello World")
  client.send(123)
}

Test.main(args)
