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

  object Service {
    import ResponseTypeClass._
    def send[A](a : A)(implicit ev : TheirResponse[A]) : Unit  = {
      println(ev.serialized(a));
    }
  }
}

object MyClient {
  import SomeoneElsesLibrary._

  class Client {
    import ResponseTypeClass._
    def send[A](x : A)(implicit ev : TheirResponse[A]) : Unit = Service.send(x) 
  }
}

object Test extends App {
  import MyClient._
  val client = new Client
  client.send("Hello World")
  client.send(123)
}

Test.main(args)
