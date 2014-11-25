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
  import SomeoneElsesLibrary._

  class Client {
    import ResponseTypeClass._
    val service = new Service
    def send[A : TheirResponse](x : A) : Unit = service.send(x) 
  }
}

object Test extends App {
  import MyClient._
  val client = new Client
  client.send("Hello World")
  client.send(123)
}

Test.main(args)
