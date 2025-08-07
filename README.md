![Banner](./assets/banner.png)

# Interval Annotated String

A tiny Android utility library that simplifies the process of creating and managing embedded
links and styles within a localised text block. It provides a clean, non-disruptive syntax that 
won't confuse translators, while still allowing you to identify specific parts of a string.

Here's a quick side-by-side comparison to the different approaches:

| Approach                  | Resource example                                                                                                                                                                                                                                                                                                                                         |
|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Interval Annotated String | <pre lang="xml">&lt;string name=&quot;privacy_policy_footer_text&quot;&gt;Visit our \[privacy policy\]\(id1\) and \[terms &amp;amp; conditions policy\]\(id2\) for more information.&lt;/string&gt;</pre>                                                                                                                                                |
| Inline HTML               | <pre lang="xml">&lt;string name=&quot;privacy_policy_footer_text&quot;&gt;lt;![CDATA[Visit our &lt;a href=&quot;https://example.com/privacy-policy&quot;&gt;privacy policy&lt;/a&gt; and &lt;a href=&quot;https://example.com/terms-and-conditions&quot;&gt;terms &amp;amp; conditions policy&lt;/a&gt; for more information.]]&gt;&lt;/string&gt;</pre> |

> [!TIP]
> Check out [our tech blog article](https://medium.com/justeattakeaway-tech/do-androids-dream-of-inlining-links-ce852efc7f87)
> to see detailed overview.

The library offers a handy API for mapping these embedded identifiers to anything you can render,
even back to a simple string! It's especially useful for converting text into to an [AnnotatedString](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString)
with custom spans, such as clickable links or different text styles.

Supports [Compose](https://developer.android.com/compose) out of the box, but is not limited to it.

## Usage

### Anatomy of an Interval Annotated String

An Interval Annotated String is a special kind of string that combines raw text with "interval annotations",
much like Markdown. Below is an example of such a string:

```
I am [an interval annotated string](id1).
```

This string is composed of two main parts:
1.  **Raw Text:** the regular text content.
2.  **Inline Interval Annotations:** these are the special annotations that define a specific interval within the string. An annotation has two components:
    * **Text Part:** the text enclosed in square brackets `[]`. This is the content that will be rendered, and it can include any characters, _even nested brackets._
    * **ID Part:** the identifier enclosed in parentheses `()`. This ID is a unique name for the interval and can only contain alphanumeric characters (similar to a variable name).
ID is crucial for identifying the interval when it's being transformed into a final, renderable string, allowing you to convert it into a proper spannable or styled text.
ID does not have any associated logic or meaning, it is just a mapping token.

These are the perfectly valid ids, 

```
helloworld, 1, id1, link99, thebeststring, 000001, jet8, -2, look_at_underscores,
i.am.point.separated
```

as well as these ids,

```
bold, italic, www.just-eat.com, 99-1, font-size, font-family
```

> [!IMPORTANT]
> **No Nested Annotations:** The library does not support nested annotations.
> If you have multiple annotations within each other, only the outermost one will be recognized and processed.
> For example, in `[outer [inner](id2)](id1)`, the text will be `outer [inner](id2)`.

### API

#### Factories

The library provides several Compose-friendly factory methods to create an **`IntervalAnnotatedString`**:

* **`intervalAnnotatedString(value: String)`**: creates an instance from a simple `String`.
* **`intervalAnnotatedString(@StringRes stringRes: Int, vararg formatArgs: Any)`**: creates an instance from a string resource, _optionally with format arguments_.
* **`pluralIntervalAnnotatedString(@PluralsRes stringRes: Int, count: Int, vararg formatArgs: Any)`**: creates an instance from a plural string resource, handling the count and _optional format arguments_.

#### Transformation

The [`IntervalAnnotatedString`](./intervalannotatedstring/src/main/java/com/justeattakeaway/intervalannotatedstring/IntervalAnnotatedString.kt) 
class provides the core functionality through its `transform` method.

```kotlin
inline fun <T> transform(onTransformText: OnTransformText<T>, onTransformInterval: OnTransformInterval<T>): T
```

* `transform` allows you to convert the `IntervalAnnotatedString` into any desired type `T`.
* `onTransformText` should create an instance of desired type `T`
* `onTransformInterval` processes the annotated intervals, providing the identifier and other details. The callback is invoked on `T` as a received.

#### Extensions

For convenience, the library includes a specialized extension function to easily 
convert an `IntervalAnnotatedString` into an `AnnotatedString`:

```kotlin
inline fun IntervalAnnotatedString.asAnnotatedString(onAddIntervalStyle: OnTransformInterval<AnnotatedString.Builder>): AnnotatedString
```

This method simplifies the process of creating a `Compose`-compatible `AnnotatedString` 
by allowing you to define the spans and annotations for each interval.

#### Exception Handling

The `IntervalAnnotatedString#transform` method may throw the following exceptions if the string format is invalid:

* `NoIdException`: thrown when an annotation has an empty ID, e.g., `"Hello [world]()"`.
* `EmptyInlineTextException`: thrown when the inline text part of an annotation is empty, e.g., `"[](id1)"`.

### Usage Example

```kotlin
private const PRIVACY_POLICY_LINK_INTERVAL_ID = "id1"

@Composable
private inline fun getPrivacyPolicyAnnotatedString(
    privacyPolicy: String,
    crossinline onPrivacyPolicyClick: () -> Unit,
) = intervalAnnotatedString(privacyPolicy)
    .asAnnotatedString { id, startsAt, length ->
        if (id == PRIVACY_POLICY_LINK_INTERVAL_ID) {
            addLink(
                clickable =
                    LinkAnnotation.Clickable(
                        tag = "URL",
                        styles =
                            TextLinkStyles(
                                style =
                                    SpanStyle(
                                        color = Color.BLUE,
                                        textDecoration = TextDecoration.Underline,
                                    ),
                            ),
                        linkInteractionListener = { onPrivacyPolicyClick() },
                    ),
                start = startsAt,
                end = startsAt + length,
            )
        }
    }
```

> [!TIP]
> Check out the sample app to see more examples.
