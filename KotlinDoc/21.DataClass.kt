    /* Data class:
    * - POJOs (Plain Old Java Objects)
    * - Immutability: mặc định là final và không thể khai báo với từ khóa open (fields có thể thay đổi với 'var' nhưng ko nên)
    * - auto-generate constructor, getter, setter, equals, hashCode, toString
    * */

    // 1. Simple: auto-generate constructor, getter, setter, equals, hashCode, toString
    data class Person(val name: String, val age: Int, val email: String, val phone: Long)

    // 2. Default and named arguments:
    data class Person(val name: String = "default name", val age: Int = 30,
                      val email: String = "dummy email", val phone: Long = 1234567890)
    // Using default arguments & named arguments
    val person1 :Person = Person("name", 25, "email@gmail.com", 555544448)
    val person2 :Person = Person()
    val person3 :Person = Person("name", 25)
    val person4 :Person = Person(name = "name", phone = 9876543210)

    // 3. Muốn một phiên bản của một đối tượng khác chỉ với một vài thay đổi -> copy()
    val person1Copy = person1.copy()
    val person1With30 = person1.copy(age = 30)
    val person4WithEmail = person4.copy(email = "person4@gmail.com")

    // 4. Inheritance: data class is final, not open -> must using interface to inherit
    interface Person {  // >< Java interface has not any fields -> sub class must override all fields, nếu ko sẽ bị Compilation error
        val name: String
        val age: Int
        val email: String
        fun hasResponsibilities() : Boolean
    }

    data class Adult(override val name: String, override val age: Int, override val email: String) : Person {
        val isMarried: Boolean = false      // new field
        val hasKids: Boolean = false        // new field
        override fun hasResponsibilities(): Boolean = true
    }

    data class Child(override val name: String, override val age: Int, override val email: String = "") : Person {
        val isInSchool: Boolean = true      // new field
        override fun hasResponsibilities(): Boolean = false
    }

    // 5. Parcelable: @Parcelize, from Kotlin 1.1.4
    @Parcelize
    data class PersonParcelize(val name: String, val age: Int, val email: String, val phone: Long) : Parcelable
