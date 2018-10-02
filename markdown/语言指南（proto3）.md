



[TOC]



# 语言指南（proto3）

本指南介绍如何使用协议缓冲区语言构建协议缓冲区数据，包括`.proto`文件语法以及如何从`.proto`文件生成数据访问类。它涵盖了协议缓冲区语言的**proto3**版本：有关较早的**proto2**语法的信息，请参阅[Proto2语言指南](https://developers.google.com/protocol-buffers/docs/proto)。

这是一个参考指南 - 对于使用本文档中描述的许多功能的分步示例，请参阅所选语言的[教程](https://developers.google.com/protocol-buffers/docs/tutorials)（目前仅限proto2;更多proto3文档即将推出）。

## 定义消息类型

先来看一个非常简单的例子。假设你想定义一个“搜索请求”的消息格式，每一个请求含有一个查询字符串、你感兴趣的查询结果所在的页数，以及每一页多少条查询结果。可以采用如下的方式来定义消息类型的.proto文件了：

```
syntax = "proto3";

message SearchRequest {
  string query = 1;
  int32 page_number = 2;
  int32 result_per_page = 3;
}
```

- 该文件的第一行指定您正在使用`proto3`语法：如果您不这样做，protobuf 编译器将假定您正在使用[proto2](https://developers.google.com/protocol-buffers/docs/proto)。这必须是文件的第一个非空的非注释行。
- 所述`SearchRequest`消息定义指定了三个字段（名称/值对），一个用于要在此类型的消息中包含的每个数据片段。每个字段都有一个名称和类型。

### 指定字段类型

在上面的示例中，所有字段都是[标量类型](https://developers.google.com/protocol-buffers/docs/proto3#scalar)：两个整数（`page_number`和`result_per_page`）和一个字符串（`query`）。但是，您还可以为字段指定合成类型，包括[枚举](https://developers.google.com/protocol-buffers/docs/proto3#enum)和其他消息类型。

### 分配标识号

正如上述文件格式，在消息定义中，每个字段都有唯一的一个**数字标识符**。这些标识符是用来在消息的[二进制格式](https://developers.google.com/protocol-buffers/docs/encoding)中识别各个字段的，一旦开始使用就不能够再改变。注：[1,15]之内的标识号在编码的时候会占用一个字节。[16,2047]之内的标识号则占用2个字节。所以应该为那些频繁出现的消息元素保留 [1,15]之内的标识号。切记：要为将来有可能添加的、频繁出现的标识号预留一些标识号。

最小的标识号可以从1开始，最大到2^29 - 1, or 536,870,911。不可以使用其中的[19000－19999]的标识号， Protobuf协议实现中对这些进行了预留。如果非要在.proto文件中使用这些预留标识号，编译时就会报警。

### 指定字段规则

消息字段可以是以下之一：

- 单数：格式良好的消息可以包含该字段中的零个或一个（但不超过一个）。
- `repeated`：此字段可以在格式良好的消息中重复任意次数（包括零）。将保留重复值的顺序。

在proto3中，`repeated`数字类型的字段默认使用`packed`编码。

`packed`您可以在[协议缓冲区编码中](https://developers.google.com/protocol-buffers/docs/encoding.html#packed)找到有关编码的更多信息。

### 添加更多消息类型

可以在单个`.proto`文件中定义多种消息类型。如果要定义多个相关消息，这很有用 - 例如，如果要定义与`SearchResponse`消息类型对应的回复消息格式，可以将其添加到相同的消息`.proto`：

```
message SearchRequest {
  string query = 1;
  int32 page_number = 2;
  int32 result_per_page = 3;
}

message SearchResponse {
 ...
}
```

### 添加注释

要为`.proto`文件添加注释，请使用C / C ++ - 样式`//`和`/* ... */`语法。

```
/ * SearchRequest表示搜索查询，带有分页选项
 *表明响应中包含哪些结果。* /

message SearchRequest {
  string query = 1;
  int32 page_number = 2; //我们想要哪个页码？
  int32 result_per_page = 3; //每页返回的结果数。
}
```

### 保留字段

如果通过完全删除字段或将其注释来[更新](https://developers.google.com/protocol-buffers/docs/proto3#updating)消息类型，则未来用户可以在对类型进行自己的更新时重用字段编号。如果以后加载相同的旧版本，这可能会导致严重问题`.proto`，包括数据损坏，隐私错误等。确保不会发生这种情况的一种方法是指定已删除字段的字段编号（和/或名称，这也可能导致JSON序列化问题）`reserved`。如果将来的任何用户尝试使用这些字段标识符，协议缓冲编译器将会抱怨。

```
message Foo {
  reserved 2, 15, 9 to 11;
  reserved "foo", "bar";
}
```

请注意，您不能在同一`reserved`语句中混合字段名称和字段编号。

### 你的生成是什么`.proto`？

当您在a上运行[协议缓冲区编译器](https://developers.google.com/protocol-buffers/docs/proto3#generating)时`.proto`，编译器会生成您所选语言的代码，您需要使用您在文件中描述的消息类型，包括获取和设置字段值，将消息序列化为输出流，并从输入流解析您的消息。

- 对于**C ++**，编译器会从每个文件生成一个`.h`和一个`.cc`文件`.proto`，并为您文件中描述的每种消息类型提供一个类。
- 对于**Java**，编译器生成一个`.java`文件，其中包含每种消息类型的类，以及`Builder`用于创建消息类实例的特殊类。
- **Python**有点不同 - Python编译器生成一个模块，其中包含每个消息类型的静态描述符，`.proto`然后与*元类*一起使用，以在运行时创建必要的Python数据访问类。
- 对于**Go**，编译器会为`.pb.go`文件中的每种消息类型生成一个类型的文件。
- 对于**Ruby**，编译器生成一个`.rb`包含消息类型的Ruby模块的文件。
- 对于**Objective-C**，编译器从每个文件生成一个`pbobjc.h`和一个`pbobjc.m`文件`.proto`，其中包含文件中描述的每种消息类型的类。
- 对于**C＃**，编译器会`.cs`从每个文件生成一个文件`.proto`，其中包含文件中描述的每种消息类型的类。

您可以按照所选语言的教程（即将推出的proto3版本）了解有关为每种语言使用API的更多信息。有关更多API详细信息，请参阅相关[API参考](https://developers.google.com/protocol-buffers/docs/reference/overview)（proto3版本即将推出）。

## 标量值类型

标量消息字段可以具有以下类型之一 - 该表显示`.proto`文件中指定的类型，以及自动生成的类中的相应类型：

| .proto type | notes                                                        | C ++ type | Java type   | Python type [2]  | Type    | Ruby type                    | C# type     | PHP type          |
| ----------- | ------------------------------------------------------------ | --------- | ----------- | ---------------- | ------- | ---------------------------- | ----------- | ----------------- |
| double      |                                                              | double    | double      | float            | float64 | float                        | double      | float             |
| float       |                                                              | float     | float       | float            | FLOAT32 | float                        | float       | float             |
| INT32       | 使用可变长度编码。编码负数的效率低 - 如果您的字段可能有负值，请改用sint32。 | INT32     | INT         | INT              | INT32   | Fixnum or Bignum (as needed) | INT         | Integer           |
| Int64       | 使用可变长度编码。编码负数的效率低 - 如果您的字段可能有负值，请改用sint64。 | Int64     | long        | int / long [3]   | Int64   | TWINS                        | long        | Integer/string[5] |
| UINT32      | 使用可变长度编码。                                           | UINT32    | int [1]     | int / long [3]   | UINT32  | Fixnum or Bignum (as needed) | UINT        | Integer           |
| UINT64      | 使用可变长度编码。                                           | UINT64    | Long [1]    | int / long [3]   | UINT64  | TWINS                        | ULONG       | Integer/string[5] |
| SINT32      | 使用可变长度编码。签名的int值。这些比常规int32更有效地编码负数。 | INT32     | INT         | INT              | INT32   | Fixnum or Bignum (as needed) | INT         | Integer           |
| sint64      | 使用可变长度编码。签名的int值。这些比常规int64更有效地编码负数。 | Int64     | long        | int / long [3]   | Int64   | TWINS                        | long        | Integer/string[5] |
| fixed32     | 总是四个字节。如果值通常大于2 28，则比uint32更有效。         | UINT32    | int [1]     | int / long [3]   | UINT32  | Fixnum or Bignum (as needed) | UINT        | Integer           |
| fixed64     | 总是八个字节。如果值通常大于2 56，则比uint64更有效。         | UINT64    | Long [1]    | int / long [3]   | UINT64  | TWINS                        | ULONG       | Integer/string[5] |
| sfixed32    | 总是四个字节。                                               | INT32     | INT         | INT              | INT32   | Fixnum or Bignum (as needed) | INT         | Integer           |
| sfixed64    | 总是八个字节。                                               | Int64     | long        | int / long [3]   | Int64   | TWINS                        | long        | Integer/string[5] |
| Boolean     |                                                              | Boolean   | Boolean     | Boolean          | Boolean | TrueClass / FalseClass       | Boolean     | Boolean           |
| string      | 字符串必须始终包含UTF-8编码或7位ASCII文本。                  | string    | string      | str / unicode[4] | string  | String (UTF-8)               | string      | string            |
| byte        | 可以包含任意字节序列。                                       | string    | Byte string | Strait           | []byte  | String (ASCII-8BIT)          | Byte string | string            |

在[协议缓冲区编码中](https://developers.google.com/protocol-buffers/docs/encoding)序列化消息时，您可以找到有关如何编码这些类型的更多信息。

[1]在Java中，无符号的32位和64位整数使用它们的带符号对应表示，最高位只是存储在符号位中。

[2]在所有情况下，将值设置为字段将执行类型检查以确保其有效。

[3] 64位或无符号32位整数在解码时始终表示为long，但如果在设置字段时给出int，则可以为int。在所有情况下，该值必须适合设置时表示的类型。见[2]。

[4] Python字符串在解码时表示为unicode，但如果给出了ASCII字符串，则可以是str（这可能会发生变化）。

[5] Integer用于64位计算机，字符串用于32位计算机。

## 默认值

解析消息时，如果编码消息不包含特定的单数元素，则解析对象中的相应字段将设置为该字段的默认值。这些默认值是特定于类型的：

- 对于字符串，默认值为空字符串。
- 对于字节，默认值为空字节。
- 对于bools，默认值为false。
- 对于数字类型，默认值为零。
- 对于[枚举](https://developers.google.com/protocol-buffers/docs/proto3#enum)，默认值是第**一个定义的枚举值**，该**值**必须为0。
- 对于消息字段，未设置该字段。它的确切值取决于语言。有关详细信息， 请参阅[生成的代码指](https://developers.google.com/protocol-buffers/docs/reference/overview)

重复字段的默认值为空（通常是相应语言的空列表）。

请注意，对于标量消息字段，一旦解析了消息，就无法确定字段是否显式设置为默认值（例如，是否设置了布尔值`false`）或者根本没有设置：您应该记住这一点在定义消息类型时。例如，`false`如果您不希望默认情况下也发生这种行为，那么在设置为时，没有一个布尔值可以启用某些行为。还要注意的是，如果一个标消息字段**被**设置为默认值，该值将不会在电线上连载。

有关默认值如何在生成的代码中工作的更多详细信息，请参阅所选语言的[生成代码指南](https://developers.google.com/protocol-buffers/docs/reference/overview)。

## 枚举

在定义消息类型时，您可能希望其中一个字段只有一个预定义的值列表。例如，假设你想添加一个 `corpus`字段每个`SearchRequest`，其中语料库可以 `UNIVERSAL`，`WEB`，`IMAGES`，`LOCAL`，`NEWS`，`PRODUCTS`或`VIDEO`。您可以非常简单地通过`enum`为每个可能的值添加一个常量来定义消息定义。

在下面的示例中，我们添加了一个带有所有可能值的`enum`调用`Corpus`，以及一个类型的字段`Corpus`：

```
message SearchRequest {
  string query = 1;
  int32 page_number = 2;
  int32 result_per_page = 3;
  enum Corpus {
    UNIVERSAL = 0;
    WEB = 1;
    IMAGES = 2;
    LOCAL = 3;
    NEWS = 4;
    PRODUCTS = 5;
    VIDEO = 6;
  }
  Corpus corpus = 4;
}
```

如您所见，`Corpus`枚举的第一个常量映射为零：每个枚举定义**必须**包含一个映射到零的常量作为其第一个元素。这是因为：

- 必须有一个零值，以便我们可以使用0作为数字[默认值](https://developers.google.com/protocol-buffers/docs/proto3#default)。
- 零值必须是第一个元素，以便与[proto2](https://developers.google.com/protocol-buffers/docs/proto)语义兼容，其中第一个枚举值始终是默认值。

您可以通过为不同的枚举常量指定相同的值来定义别名。为此，您需要将`allow_alias`选项设置为`true`，否则协议编译器将在找到别名时生成错误消息。

```
enum EnumAllowingAlias {
  option allow_alias = true;
  UNKNOWN = 0;
  STARTED = 1;
  RUNNING = 1;
}
enum EnumNotAllowingAlias {
  UNKNOWN = 0;
  STARTED = 1;
  // RUNNING = 1;  // Uncommenting this line will cause a compile error inside Google and a warning message outside.
}
```

枚举器常量必须在32位整数范围内。由于`enum`值在线上使用[varint编码](https://developers.google.com/protocol-buffers/docs/encoding)，因此负值效率低，因此不建议使用。您可以`enum`在消息定义中定义s，如上例所示，`enum`也可以在外部定义 - 这些可以在`.proto`文件的任何消息定义中重用。您还可以使用`enum`语法将一个消息中声明的类型用作另一个消息中的字段类型。 `*MessageType*.*EnumType*`

当你在`.proto`使用a的协议缓冲编译器上运行时`enum`，生成的代码将具有`enum`Java或C ++ 的相应代码，这`EnumDescriptor`是Python的一个特殊类，用于在运行时生成的类中创建一组带有整数值的符号常量。

在反序列化期间，将在消息中保留无法识别的枚举值，但是当反序列化消息时，如何表示这种值取决于语言。在支持具有超出指定符号范围的值的开放枚举类型的语言中，例如C ++和Go，未知的枚举值仅作为其基础整数表示存储。在具有封闭枚举类型（如Java）的语言中，枚举中的大小写用于表示无法识别的值，并且可以使用特殊访问器访问基础整数。在任何一种情况下，如果消息被序列化，则仍然会使用消息序列化无法识别的值。

有关如何`enum`在应用程序中使用消息的详细信息，请参阅所选语言的[生成代码指南](https://developers.google.com/protocol-buffers/docs/reference/overview)。

### 保留值

如果通过完全删除枚举条目或将其注释掉来[更新](https://developers.google.com/protocol-buffers/docs/proto3#updating)枚举类型，则未来用户可以在对类型进行自己的更新时重用该数值。如果以后加载相同的旧版本，这可能会导致严重问题`.proto`，包括数据损坏，隐私错误等。确保不会发生这种情况的一种方法是指定已删除条目的数值（和/或名称，这也可能导致JSON序列化问题）`reserved`。如果将来的任何用户尝试使用这些标识符，协议缓冲编译器将会抱怨。您可以使用`max`关键字指定保留的数值范围达到最大可能值。

```
enum Foo {
  reserved 2, 15, 9 to 11, 40 to max;
  reserved "FOO", "BAR";
}
```

请注意，您不能在同一`reserved`语句中混合字段名称和数值。

## 使用其他消息类型

您可以使用其他消息类型作为字段类型。例如，假设你想包括`Result`每个消息的`SearchResponse`消息-要做到这一点，你可以定义一个`Result`在同一个消息类型`.proto`，然后指定类型的字段`Result`中`SearchResponse`：

```
message SearchResponse {
  repeated Result results = 1;
}

message Result {
  string url = 1;
  string title = 2;
  repeated string snippets = 3;
}
```

### 导入定义

在上面的示例中，`Result`消息类型在同一文件中定义`SearchResponse`- 如果要用作字段类型的消息类型已在另一个`.proto`文件中定义，该怎么办？

您可以`.proto`通过*导入*来使用其他文件中的定义。要导入其他`.proto`人的定义，请在文件顶部添加import语句：

```
import“myproject / other_protos.proto”;
```

默认情况下，您只能使用直接导入`.proto`文件中的定义。但是，有时您可能需要将`.proto`文件移动到新位置。`.proto`现在，您可以`.proto`在旧位置放置一个虚拟文件，以使用该`import public`概念将所有导入转发到新位置，而不是直接移动文件并在一次更改中更新所有调用站点。`import public`任何导入包含该`import public`语句的proto的人都可以传递依赖关系。例如：

```
// new.proto
// All definitions are moved here
```

```
// old.proto
//This is the proto that all clients are importing.
import public“new.proto”;
import“other.proto”;
```
```
// client.proto
import "old.proto";
//您使用old.proto和new.proto中的定义，但不使用other.proto
```

协议编译器使用`-I`/ `--proto_path`flag 在协议编译器命令行中指定的一组目录中搜索导入的文件 。如果没有给出标志，它将查找调用编译器的目录。通常，您应该将`--proto_path`标志设置为项目的根目录，并对所有导入使用完全限定名称。

### 使用proto2消息类型

可以导入[proto2](https://developers.google.com/protocol-buffers/docs/proto)消息类型并在proto3消息中使用它们，反之亦然。但是，proto2枚举不能直接用于proto3语法（如果导入的proto2消息使用它们就可以了）。

## 嵌套类型

您可以在其他消息类型中定义和使用消息类型，如下例所示 - 此处`Result`消息在消息中定义`SearchResponse`：

```
message SearchResponse {
  message Result {
    string url = 1;
    string title = 2;
    repeated string snippets = 3;
  }
  repeated Result results = 1;
}
```

如果要在其父消息类型之外重用此消息类型，请将其称为： `*Parent*.*Type*`

```
message SomeOtherMessage {
  SearchResponse.Result result = 1;
}
```

您可以根据需要深入嵌套消息：

```
message Outer {                  // Level 0
  message MiddleAA {  // Level 1
    message Inner {   // Level 2
      int64 ival = 1;
      bool  booly = 2;
    }
  }
  message MiddleBB {  // Level 1
    message Inner {   // Level 2
      int32 ival = 1;
      bool  booly = 2;
    }
  }
}
```

## 更新消息类型

如果现有的消息类型不再满足您的所有需求 - 例如，您希望消息格式具有额外的字段 - 但您仍然希望使用使用旧格式创建的代码，请不要担心！在不破坏任何现有代码的情况下更新消息类型非常简单。请记住以下规则：

- 请勿更改任何现有字段的字段编号。
- 如果添加新字段，则使用“旧”消息格式按代码序列化的任何消息仍可由新生成的代码进行解析。您应该记住这些元素的[默认值](https://developers.google.com/protocol-buffers/docs/proto3#default)，以便新代码可以正确地与旧代码生成的消息进行交互。同样，您的新代码创建的消息可以由旧代码解析：旧的二进制文件在解析时只是忽略新字段。有关详细信息，请参阅“ [未知字段”](https://developers.google.com/protocol-buffers/docs/proto3#unknowns)部分
- 只要在更新的消息类型中不再使用字段编号，就可以删除字段。您可能希望重命名该字段，可能添加前缀“OBSOLETE_”，或者[保留](https://developers.google.com/protocol-buffers/docs/proto3#reserved)字段编号，以便您的未来用户`.proto`不会意外地重复使用该编号。
- `int32`，`uint32`，`int64`，`uint64`，和`bool`都是兼容的-这意味着你可以改变这些类型到另一个的一个场不破坏forwards-或向后兼容。如果从导线中解析出一个不符合相应类型的数字，您将获得与在C ++中将该数字转换为该类型相同的效果（例如，如果将64位数字作为int32读取，它将被截断为32位）。
- `sint32`并且`sint64`彼此兼容但与其他整数类型*不*兼容。
- `string``bytes`只要字节是有效的UTF-8 ，它们是兼容的。
- `bytes`如果字节包含消息的编码版本，则嵌入消息是兼容的。
- `fixed32`与兼容`sfixed32`，并`fixed64`用`sfixed64`。
- `enum`与兼容`int32`，`uint32`，`int64`，和`uint64`电线格式条款（注意，如果他们不适合的值将被截断）。但请注意，在反序列化消息时，客户端代码可能会以不同方式对待它们：例如，`enum`将在消息中保留未识别的proto3 类型，但在反序列化消息时如何表示这种类型取决于语言。Int字段总是保留它们的价值。
- 将单个值更改为**新** 成员`oneof`是安全且二进制兼容的。`oneof`如果您确定没有代码一次设置多个字段，则将多个字段移动到新字段可能是安全的。将任何字段移动到现有字段`oneof`并不安全。

## 未知字段

未知字段是格式良好的协议缓冲区序列化数据，表示解析器无法识别的字段。例如，当旧二进制文件解析具有新字段的新二进制文件发送的数据时，这些新字段将成为旧二进制文件中的未知字段。

最初，proto3消息在解析期间总是丢弃未知字段，但在3.5版本中，我们重新引入了保存未知字段以匹配proto2行为。在版本3.5及更高版本中，未知字段在解析期间保留并包含在序列化输出中。

## 任何

该`Any`消息类型，可以使用邮件作为嵌入式类型，而不必自己.proto定义。一个`Any`含有任意的序列化消息`bytes`，以充当一个全局唯一标识符和解析到该消息的类型的URL一起。要使用该`Any`类型，您需要[导入](https://developers.google.com/protocol-buffers/docs/proto3#other)`google/protobuf/any.proto`。

```
import "google/protobuf/any.proto";

message ErrorStatus {
  string message = 1;
  repeated google.protobuf.Any details = 2;
}
```

给定消息类型的默认类型URL是。 `type.googleapis.com/*packagename*.*messagename*`

不同的语言实现将支持运行时库佣工类型安全的方式打包和解包的任何值-例如，在Java中，任何类型都会有特殊`pack()`和`unpack()`存取，而在C ++中有`PackFrom()`和`UnpackTo()`方法：

```
// Storing an arbitrary message type in Any.
NetworkErrorDetails details = ...;
ErrorStatus status;
status.add_details()->PackFrom(details);

// Reading an arbitrary message from Any.
ErrorStatus status = ...;
for (const Any& detail : status.details()) {
  if (detail.Is<NetworkErrorDetails>()) {
    NetworkErrorDetails network_error;
    detail.UnpackTo(&network_error);
    ... processing network_error ...
  }
}
```

**目前，正在开发用于处理Any类型的运行时库**。

如果您已熟悉[proto2语法](https://developers.google.com/protocol-buffers/docs/proto)，则Any类型将替换[扩展](https://developers.google.com/protocol-buffers/docs/proto#extensions)。

## Oneof

如果您有一个包含许多字段的消息，并且最多只能同时设置一个字段，则可以使用oneof功能强制执行此行为并节省内存。

除了一个共享内存中的所有字段之外，其中一个字段类似于常规字段，并且最多可以同时设置一个字段。设置oneof的任何成员会自动清除所有其他成员。您可以使用特殊`case()`或`WhichOneof()`方法检查oneof中的哪个值（如果有），具体取决于您选择的语言。

### 使用Oneof

要在您中定义oneof，请`.proto`使用`oneof`关键字后跟您的oneof名称，在这种情况下`test_oneof`：

```
message SampleMessage {
  oneof test_oneof {
    string name = 4;
    SubMessage sub_message = 9;
  }
}
```

然后，将oneof字段添加到oneof定义中。您可以添加任何类型的字段，但不能使用`repeated`字段。

在生成的代码中，oneof字段与常规字段具有相同的getter和setter。您还可以使用特殊方法检查oneof中的值（如果有）。您可以在相关[API参考中](https://developers.google.com/protocol-buffers/docs/reference/overview)找到有关所选语言的oneof API的更多信息。

### 其中一个特点

- 设置oneof字段将自动清除oneof的所有其他成员。因此，如果您设置了多个字段，则只有您设置的

  最后一个

  字段仍然具有值。

  ```
  SampleMessage message;
  message.set_name("name");
  CHECK(message.has_name());
  message.mutable_sub_message();   // Will clear name field.
  CHECK(!message.has_name());
  ```

- 如果解析器在线路上遇到同一个oneof的多个成员，则在解析的消息中仅使用看到的最后一个成员。

- 一个不可能`repeated`。

- Reflection API适用于其中一个字段。

- 如果您使用的是C ++，请确保您的代码不会导致内存崩溃。以下示例代码将崩溃，`sub_message`

  已通过调用该` set_name()`方法删除了该代码。

  ```
  SampleMessage message;
  SubMessage* sub_message = message.mutable_sub_message();
  message.set_name("name");      // Will delete sub_message
  sub_message->set_...            // Crashes here 
  ```

- 同样在C ++中，如果你有`Swap()`两个消息与oneofs，每个消息最终将与另一个消息结果：在下面的例子中，`msg1`将有一个`sub_message`，`msg2`并将有一`name`。

  ```
  SampleMessage msg1;
  msg1.set_name("name");
  SampleMessage msg2;
  msg2.mutable_sub_message();
  msg1.swap(&msg2);
  CHECK(msg1.has_sub_message());
  CHECK(msg2.has_name());
  ```

### 向后兼容性问题

添加或删除其中一个字段时要小心。如果检查oneof返回的值`None`/ `NOT_SET`，这可能意味着oneof尚未设置或已在不同版本的oneof的被设置为一个字段。没有办法区分，因为没有办法知道线上的未知字段是否是其中一个成员。

#### 标签重用问题

- **将字段移入或移出oneof**：在序列化和解析消息后，您可能会丢失一些信息（某些字段将被清除）。但是，您可以安全地将单个字段移动到**新的** oneof中，并且如果已知只有一个字段被设置，则可以移动多个字段。
- **删除oneof字段并将其添加回**：在序列化和解析消息后，这可能会清除当前设置的oneof字段。
- **拆分或合并oneof**：这与移动常规字段有类似的问题。

## 地图

如果要在数据定义中创建关联映射，协议缓冲区提供了一种方便的快捷方式语法：

```
map < key_type ，value_type > map_field = N ;
```

...其中`key_type`可以是任何整数或字符串类型（因此，除了浮点类型之外的任何[标量](https://developers.google.com/protocol-buffers/docs/proto3#scalar)类型`bytes`）。请注意，枚举不是有效的`key_type`。的`value_type`可以是任何类型的除另一地图。

因此，例如，如果要创建项目映射，其中每条`Project`消息都与字符串键相关联，则可以像下面这样定义它：

```
map < string ，Project > projects = 3 ;  
```

- 地图字段不能`repeated`。
- 地图值的有线格式排序和地图迭代排序未定义，因此您不能依赖于特定顺序的地图项目。
- 为a生成文本格式时`.proto`，地图按键排序。数字键按数字排序。
- 从线路解析或合并时，如果有重复的映射键，则使用最后看到的键。从文本格式解析映射时，如果存在重复键，则解析可能会失败。
- 如果为映射字段提供键但没有值，则字段序列化时的行为取决于语言。在C ++，Java和Python中，类型的默认值是序列化的，而在其他语言中没有任何序列化。

生成的地图API目前可用于所有proto3支持的语言。您可以在相关[API参考中](https://developers.google.com/protocol-buffers/docs/reference/overview)找到有关所选语言的地图API的更多信息。

### 向后兼容性

映射语法在线上等效于以下内容，因此不支持映射的协议缓冲区实现仍可处理您的数据：

```
message MapFieldEntry {
  key_type key = 1;
  value_type value = 2;
}

repeated MapFieldEntry map_field = N;
```

任何支持映射的协议缓冲区实现都必须生成和接受上述定义可以接受的数据。

## 包

您可以向`.proto`文件添加`package`可选说明符，以防止协议消息类型之间的名称冲突。

```
package foo.bar;
message Open { ... }
```

然后，您可以在定义消息类型的字段时使用包说明符：

```
message Foo {
  ...
  foo.bar.Open open = 1;
  ...
}
```

包说明符影响生成的代码的方式取决于您选择的语言：

- 在**C ++中**，生成的类包含在C ++命名空间中。例如，`Open`将在命名空间中`foo::bar`。
- 在**Java中**，该包用作Java包，除非您`option java_package`在`.proto`文件中明确提供了该包。
- 在**Python中**，package指令被忽略，因为Python模块是根据它们在文件系统中的位置进行组织的。
- 在**Go中**，该包用作Go包名称，除非您`option go_package`在`.proto`文件中明确提供。
- 在**Ruby中**，生成的类包含在嵌套的Ruby命名空间内，转换为所需的Ruby大写形式（首字母大写;如果第一个字符不是字母，`PB_`则前置）。例如，`Open`将在命名空间中`Foo::Bar`。
- 在**C＃中**，包转换为PascalCase后用作命名空间，除非您`option csharp_namespace`在`.proto`文件中明确提供。例如，`Open`将在命名空间中`Foo.Bar`。

### 包和名称解析

协议缓冲区语言中的类型名称解析与C ++类似：首先搜索最里面的范围，然后搜索下一个范围，依此类推，每个包被认为是其父包的“内部”。一个领先的'。' （例如，`.foo.bar.Baz`）意味着从最外层的范围开始。

protobuf 编译器通过解析导入的`.proto`文件来解析所有类型名称。每种语言的代码生成器都知道如何使用该语言引用每种类型，即使它具有不同的范围规则。

## 定义服务

如果要将消息类型与RPC（远程过程调用）系统一起使用，则可以在`.proto`文件中定义RPC服务接口，protobuf 编译器将使用您选择的语言生成服务接口代码和存根。因此，例如，如果要定义RPC服务请求方法为:`SearchRequest`和返回方法为:`SearchResponse`，可以`.proto`按如下方式在文件中定义它：

```
service SearchService {
  rpc Search（SearchRequest）returns（SearchResponse）;
}
```

与协议缓冲区一起使用的最简单的RPC系统是[gRPC](https://grpc.io/)：一种由Google开发的，平台中立的开源RPC系统。gRPC特别适用于protobuf，并允许在您的`.proto`文件中使用特殊的protobuf 编译器插件直接生成相关的RPC代码。

如果您不想使用gRPC，也可以将protobuf与您自己的RPC实现一起使用。您可以在[Proto2语言指南中](https://developers.google.com/protocol-buffers/docs/proto#services)找到更多相关信息。

还有一些正在进行的第三方项目使用Protocol Buffers开发RPC实现。有关我们了解的项目的链接列表，请参阅[第三方加载项wiki页面](https://github.com/google/protobuf/blob/master/docs/third_party.md)。

## JSON映射

Proto3支持JSON中的规范编码，使得在系统之间共享数据变得更加容易。在下表中逐个类型地描述编码。

如果JSON编码数据中缺少值`null`，或者其值为，则在解析为协议缓冲区时，它将被解释为适当的[默认值](https://developers.google.com/protocol-buffers/docs/proto3#default)。如果字段在协议缓冲区中具有默认值，则默认情况下将在JSON编码数据中省略该字段以节省空间。实现可以提供用于在JSON编码的输出中发出具有默认值的字段的选项。

| proto3                 | JSON          | JSON示例                                 | 笔记                                                         |
| ---------------------- | ------------- | ---------------------------------------- | ------------------------------------------------------------ |
| message                | object        | `{"fooBar": v, "g": null,…}`             | 生成JSON对象。消息字段名称映射到小写驼峰并成为JSON对象键。如果`json_name`指定了field选项，则指定的值将用作键。解析器接受小写驼峰名称（或`json_name`选项指定的名称）和原始proto字段名称。`null`是所有字段类型的可接受值，并将其视为相应字段类型的默认值。 |
| eunm                   | String        | `"FOO_BAR"`                              | 使用proto中指定的枚举值的名称。解析器接受枚举名称和整数值。  |
| map<K，V>              | object        | `{"k": v, …}`                            | 所有键都转换为字符串。                                       |
| repeated V.            | array         | `[v, …]`                                 | `null` 被接受为空列表[]。                                    |
| bool                   | true,false    | `true, false`                            |                                                              |
| string                 | string        | `"Hello World!"`                         |                                                              |
| bytes                  | base64 string | `"YWJjMTIzIT8kKiYoKSctPUB+"`             | JSON值将是使用带填充的标准base64编码编码为字符串的数据。接受带有/不带填充的标准或URL安全base64编码。 |
| int32，fixed32，uint32 | string        | `1, -10, 0`                              | JSON值将是十进制数。接受数字或字符串。                       |
| int64，fixed64，uint64 | string        | `"1", "-10"`                             | JSON值将是十进制字符串。接受数字或字符串。                   |
| float,double           | number        | `1.1, -10.0, 0, "NaN","Infinity"`        | JSON值将是一个数字或一个特殊字符串值“NaN”，“Infinity”和“-Infinity”。接受数字或字符串。指数表示法也被接受。 |
| any                    | object        | `{"@type": "url", "f": v, … }`           | 如果Any包含具有特殊JSON映射的值，则将按如下方式进行转换：。否则，该值将转换为JSON对象，并将插入该字段以指示实际的数据类型。`{"@type": xxx, "value": yyy}``"@type"` |
| Timestamp              | string        | `"1972-01-01T10:00:20.021Z"`             | 使用RFC 3339，其中生成的输出将始终被Z标准化并使用0,3,6或9个小数位。也接受“Z”以外的偏移。 |
| Duration               | string        | `"1.000340012s", "1s"`                   | 生成的输出始终包含0,3,6或9个小数位，具体取决于所需的精度，后跟后缀“s”。接受的是任何小数位（也没有），只要它们符合纳秒精度并且后缀“s”是必需的。 |
| Struct                 | `object`      | `{ … }`                                  | 任何JSON对象。见。`struct.proto`                             |
| Wrapper types          | various types | `2, "2", "foo", true,"true", null, 0, …` | 包装器在JSON中使用与包装基元类型相同的表示形式，除了`null`在数据转换和传输期间允许和保留的表示形式。 |
| FieldMask              | string        | `"f.fooBar,h"`                           | 见。`field_mask.proto`                                       |
| ListValue              | array         | `[foo, bar, …]`                          |                                                              |
| Value                  | value         |                                          | 任何JSON值                                                   |
| NullValue              | null          |                                          | JSON null                                                    |

### JSON选项

proto3  JSON实现可以提供以下选项：

- **使用默认值发出字段**：默认情况下，proto3 JSON输出中省略了**具有默认值的**字段。实现可以提供覆盖此行为的选项，并使用其默认值输出字段。
- **忽略未知字段**：默认情况下，Proto3 JSON解析器应拒绝未知字段，但可以提供忽略解析中未知字段的选项。
- **使用proto字段名称而不是小写驼峰名称**：默认情况下，proto3 JSON打印机应将字段名称转换为小写驼峰并将其用作JSON名称。实现可以提供使用proto字段名称作为JSON名称的选项。Proto3 JSON解析器需要接受转换后的小写驼峰名称和proto字段名称。
- **将枚举值发送为整数而不是字符串**：默认情况下，在JSON输出中使用枚举值的名称。可以提供选项以使用枚举值的数值。

## 选项

`.proto`文件中的各个声明可以使用许多*选项*进行注释。选项不会更改声明的整体含义，但可能会影响在特定上下文中处理它的方式。可用选项的完整列表在中定义`google/protobuf/descriptor.proto`。

一些选项是文件级选项，这意味着它们应该在顶级范围内编写，而不是在任何消息，枚举或服务定义中。一些选项是消息级选项，这意味着它们应该写在消息定义中。一些选项是字段级选项，这意味着它们应该写在字段定义中。选项也可以写在枚举类型，枚举值，服务类型和服务方法上; 但是，目前没有任何有用的选择。

以下是一些最常用的选项：

- `java_package`（文件选项）：用于生成的Java类的包。如果`.proto`文件中没有给出显式选项`java_package`，则默认情况下将使用proto包（使用文件中的“package”关键字指定  .proto  ）。但是，proto包通常不能生成好的Java包，因为proto包不会以反向域名开头。如果不生成Java代码，则此选项无效。

  ```
  option java_package =“com.example.foo”;
  ```

- `java_multiple_files` （文件选项）：导致在包级别定义顶级消息，枚举和服务，而不是在.proto文件之后命名的外部类中。

```
option java_multiple_files = true;
```

- `java_outer_classname`（file option）：要生成的最外层Java类（以及文件名）的类名。如果  `.proto`文件中没有指定 `java_outer_classname`，则通过将`  .proto
    `文件名转换为驼峰格式（因此 `
    foo_bar.proto  ` 成为` FooBar.java  `）来构造类名。如果不生成Java代码，则此选项无效。

  ```
  option java_outer_classname =“Ponycopter”;
  ```

- `
  optimize_for
  `

  （文件选项）：可以设置为`SPEED`，`CODE_SIZE`或`LITE_RUNTIME`。这会以下列方式影响C ++和Java代码生成器（可能还有第三方生成器）：

  - `SPEED`（默认值）：protobuf 编译器将生成用于对消息类型进行序列化，解析和执行其他常见操作的代码。此代码经过高度优化。
  - `CODE_SIZE`：protobuf 编译器将生成最少的类，并依赖于基于反射的共享代码来实现序列化，解析和各种其他操作。因此生成的代码比使用`SPEED`小得多，但操作会更慢。类仍将实现与`SPEED`模式完全相同的公共API 。此模式在包含非常大数量的`.proto`文件的应用程序中最有用，并且不需要所有文件都非常快速。
  - `LITE_RUNTIME`：protobuf 编译器将生成仅依赖于“lite”运行时库（`libprotobuf-lite`而不是`libprotobuf`）的类。精简版运行时比整个库小得多（大约小一个数量级），但省略了描述符和反射等特定功能。这对于在移动电话等受限平台上运行的应用程序尤其有用。编译器仍然会像在`SPEED`模式中一样生成所有方法的快速实现。生成的类将仅实现`MessageLite`每种语言的接口，该接口仅提供完整`Message`接口的方法的子集。

  ```
  option optimize_for = CODE_SIZE;
  ```

- `cc_enable_arenas`（文件选项）：为C ++生成的代码启用[竞技场分配](https://developers.google.com/protocol-buffers/docs/reference/arenas)。

- `objc_class_prefix`（文件选项）：设置Objective-C类前缀，该前缀预先添加到此.proto的所有Objective-C生成的类和枚举中。没有默认值。您应该使用[Apple建议的](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/ProgrammingWithObjectiveC/Conventions/Conventions.html#//apple_ref/doc/uid/TP40011210-CH10-SW4) 3-5个大写字符之间的前缀。请注意，Apple保留所有2个字母的前缀。

- `deprecated`（字段选项）：如果设置为`true`，则表示该字段已弃用，新代码不应使用该字段。在大多数语言中，这没有实际效果。在Java中，这成为一个`@Deprecated`注释。将来，其他特定于语言的代码生成器可能会在字段的访问器上生成弃用注释，这将导致在编译尝试使用该字段的代码时发出警告。如果任何人都没有使用该字段，并且您希望阻止新用户使用该字段，请考虑使用保留语句替换字段声明。

  ```
  int32 old_field = 6 [deprecated = true];
  ```

### 自定义选项

Protocol Buffers还允许您定义和使用自己的选项。这是大多数人不需要的**高级功能**。如果您确实认为需要创建自己的选项，请参阅[Proto2语言指南](https://developers.google.com/protocol-buffers/docs/proto.html#customoptions)以获取详细信息。请注意，创建自定义选项使用的[扩展名](https://developers.google.com/protocol-buffers/docs/proto.html#extensions)仅允许用于proto3中的自定义选项。

## 生成您的类

根据实际工作需要，生成以下对应语言的自定义消息类型Java，Python，C ++，Go, Ruby, Objective-C，或C＃的`.proto`文件，你需要运行protobuf 编译器`protoc`上`.proto`。如果尚未安装编译器，请[下载该软件包](https://developers.google.com/protocol-buffers/docs/downloads.html)并按照自述文件中的说明进行操作。对于Go，您还需要为编译器安装一个特殊的代码生成器插件：您可以在GitHub上的[golang / protobuf](https://github.com/golang/protobuf/)存储库中找到这个和安装说明。

Protobuf 编译器的调用如下：

```
protoc --proto_path = IMPORT_PATH --cpp_out = DST_DIR --java_out = DST_DIR --python_out = DST_DIR --go_out = DST_DIR --ruby_out = DST_DIR --objc_out = DST_DIR --csharp_out = DST_DIR  path / to / file .proto
```

- `IMPORT_PATH`指定`.proto`解析`import`指令时在其中查找文件的目录。如果省略，则使用当前目录。可以通过`--proto_path`多次传递选项来指定多个导入目录; 他们将按顺序搜索。 可以用作简短的形式。 `-I=*IMPORT_PATH*``--proto_path`

- 您可以提供一个或多个输出指令：

  - `--cpp_out`生成C ++代码`DST_DIR`。有关更多信息，请参阅[C ++生成的代码参考](https://developers.google.com/protocol-buffers/docs/reference/cpp-generated)。
  - `--java_out`生成Java代码`DST_DIR`。请参阅[的Java生成的代码参考](https://developers.google.com/protocol-buffers/docs/reference/java-generated)更多。
  - `--python_out`生成Python代码`DST_DIR`。看到[的Python生成的代码的参考](https://developers.google.com/protocol-buffers/docs/reference/python-generated)更多。
  - `--go_out`生成Go代码`DST_DIR`。有关更多信息，请参阅[Go生成代码参考](https://developers.google.com/protocol-buffers/docs/reference/go-generated)。
  - `--ruby_out`生成Ruby代码`DST_DIR`。Ruby生成的代码参考即将推出！
  - `--objc_out`生成Objective-C代码`DST_DIR`。有关更多信息，请参阅[Objective-C生成的代码参考](https://developers.google.com/protocol-buffers/docs/reference/objective-c-generated)。
  - `--csharp_out`生成C＃代码`DST_DIR`。有关更多信息，请参阅[C＃生成的代码参考](https://developers.google.com/protocol-buffers/docs/reference/csharp-generated)。
  - `--php_out`生成PHP代码`DST_DIR`。看到[PHP生成的代码的参考](https://developers.google.com/protocol-buffers/docs/reference/php-generated)更多。

  为了方便起见，如果DST_DIR结束于`.zip`或.`jar`，编译器会将输出写入具有给定名称的单个ZIP格式存档文件。`.jar`输出还将根据Java JAR规范的要求提供清单文件。请注意，如果输出存档已存在，则会被覆盖; 编译器不够智能，无法将文件添加到现有存档中。

- 您必须提供一个或多个`.proto`文件作为输入。`.proto`可以一次指定多个文件。虽然文件是相对于当前目录命名的，但每个文件必须位于其中一个文件中，`IMPORT_PATH`以便编译器可以确定其规范名称。