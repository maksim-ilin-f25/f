use serde::Serialize;

use crate::{FAtom, FKeyword, FList, FLiteral};

#[derive(Debug, Clone, Serialize)]
#[serde(rename_all = "lowercase")]
pub enum FElement {
    Literal(FLiteral),
    Keyword(FKeyword),
    Atom(FAtom),
    List(FList),
    Quote(Box<FElement>),
}
