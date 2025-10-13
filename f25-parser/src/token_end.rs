use chumsky::prelude::*;

pub fn token_end<'src>()
-> impl Parser<'src, &'src str, &'src str, extra::Err<Rich<'src, char>>> + Clone {
    choice((
        text::whitespace().at_least(1).to_slice(),
        one_of("'()").to_slice(),
    ))
}
