# Testing Reference

Tests for IDE Features

|              | Status | Note                               |
|--------------|--------|------------------------------------|
| Annotator    | ✅      | Tested Variables colors            |
| Colors       | N/A    |                                    |
| Commenter    | ✅      | Tested line and block comments     |
| Files        | N/A    |                                    |
| Highlight    | N/A    |                                    |
| LineMarkers  | ❌      | Ensure elements have their markers |
| Presentation | ❌      | Check the presentation             |
| Settings     | N/A    |                                    |
| Spelling     | N/A    |                                    |
| Structure    | ✅      | Test Let/Val, only basic/pattern   |
| Template     | ❌      | Test context                       |
| Typing       | N/A    |                                    |

**Note**: extract `#getChildren` from Structure and test it.

Tests for Parser

|      | Parser | Mixin | Stubs | Indexes |
|------|--------|-------|-------|---------|
| File | N/A    | N/A   | ❌     | ❌       |
| Let  | ❌      | ✅     | ❌     | ❌       |
| Val  | ❌      | ❌     | ❌     | ❌       |

Notes

* `val _ : type`: invalid
* `val _0 : type`: valid