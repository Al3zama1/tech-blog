import React from 'react'
import { Link } from 'react-router-dom'
import Image from '../components/Image'
import PostMenuActions from '../components/PostMenuActions'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faGithub } from '@fortawesome/free-brands-svg-icons/faGithub'
import { faLinkedin } from '@fortawesome/free-brands-svg-icons'
import Search from '../components/Search'
import Comments from '../components/Comments'

const SinglePostPage = () => {
  return (
    <div className='flex flex-col gap-8'>
        <div className='flex gap-8'>
            <div className='lg:w-3/5 flex flex-col gap-8'>
                <h1 className='text-xl md:text-3xl xl:text-4xl 2xl:text-5xl font-semibold'>Spring Boot Exception Handling</h1>
                <div className='flex items-center gap-2 text-gray-400 text-sm'>
                    <span>Written by </span>
                    <Link to='/test' className='text-blue-800'>John Doe</Link>
                    <span>on</span>
                    <Link to='/test' className='text-blue-800'>Programming</Link>
                    <span>2 days agos</span>
                </div>
                <p className='text-gray-500 font-medium'>
                    Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quaerat laudantium adipisci animi asperiores magnam voluptas consequatur, temporibus fugiat iste optio. Itaque at provident perspiciatis sunt, omnis deleniti earum facere. Voluptatibus!
                </p>
            </div>
            <div className='hidden lg:block w-2/5'>
                <Image src='/tech-blog/postImg.jpeg' w='600' className='rounded-2xl' />
            </div>
        </div>
        <div className='flex flex-col md:flex-row gap-12'>
            <div className='lg:text-lg flex flex-col gap-6 text-justify'>
                <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Iure, voluptates earum, ratione autem dolore tenetur, placeat sint ad non rerum quis eligendi voluptas consectetur blanditiis quisquam amet. Quas tenetur illo rem dolore ipsum aperiam mollitia, provident animi, repellat nisi officia, hic omnis perferendis molestias pariatur culpa. Corporis officia praesentium laudantium, laboriosam dignissimos, doloremque accusamus dicta veniam ullam excepturi esse velit perferendis est recusandae repudiandae, assumenda officiis veritatis molestias aliquam omnis culpa? Iste at odio iusto, neque fugit soluta velit molestiae, deleniti, consequuntur doloribus quidem facilis nobis! Ut, nesciunt praesentium deserunt odio impedit quidem atque incidunt, natus odit culpa est neque.
                </p>
                <p>
                    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Ducimus quod nostrum sint deserunt earum saepe pariatur! Voluptatem, facere veniam quia ea dolor placeat doloribus vero, error praesentium laboriosam id sapiente nisi hic ipsa cumque, atque aliquam explicabo non? Consectetur, labore.
                </p>
                <p>
                    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Distinctio blanditiis soluta corporis dolores earum minus placeat commodi, tenetur labore repellendus iste assumenda molestiae necessitatibus aperiam aliquam natus nemo optio, dolorem animi temporibus debitis voluptas. Suscipit voluptatem aliquam dolore expedita repellat, amet cupiditate hic ad, animi, consectetur maiores dolorum ipsa enim praesentium quisquam dicta? Magnam aliquid saepe fuga culpa quod rerum?
                </p>
                <p>
                    Lorem ipsum dolor sit, amet consectetur adipisicing elit. At non nulla nobis nam blanditiis asperiores tempore, commodi quae autem rerum animi, eius numquam natus expedita beatae dignissimos eaque similique aperiam soluta officia. Quasi eius perferendis laudantium nihil reiciendis, temporibus nemo praesentium unde minima animi amet, sint corporis. Illo aut nemo odio quo omnis incidunt recusandae inventore aspernatur ab, reiciendis laboriosam, id debitis accusantium doloribus vel temporibus? Reprehenderit, quae ratione minima facilis voluptatum autem non at quaerat odio laborum consequatur et temporibus ullam atque odit! Veritatis, corporis debitis quo quis veniam omnis labore alias ad aut, iusto enim. In, a recusandae!
                </p>
                <p>
                    Lorem ipsum, dolor sit amet consectetur adipisicing elit. Voluptatibus error odio sit quia libero doloremque neque, possimus odit laborum minima adipisci facilis, quibusdam ducimus, cumque nisi nobis! Iure doloribus, earum voluptas cupiditate repudiandae unde et eius accusamus quae quo modi harum distinctio! Quaerat iusto sint perspiciatis doloremque odio sed, aperiam harum nobis nam assumenda sapiente aliquam! Necessitatibus facilis sint voluptates dicta inventore aliquam, autem, minima adipisci tempora nesciunt laborum quod, ut porro! Aspernatur consectetur quia delectus ducimus hic iure odit, natus dicta harum accusamus recusandae itaque rerum illo! Quasi amet qui iste ipsum assumenda quas distinctio quia soluta debitis, reprehenderit officia laudantium voluptates autem necessitatibus dolor at atque veniam hic corrupti laboriosam iusto natus facilis? Numquam ipsa praesentium dicta in?
                </p>
                <p>
                    Lorem ipsum dolor sit, amet consectetur adipisicing elit. Alias, dignissimos iste nihil explicabo, facilis beatae, ut sint fuga cumque autem qui repudiandae iusto. Id vero magni voluptates. Ducimus tempore facere deleniti nihil totam? Distinctio error enim, asperiores eligendi aperiam nam aspernatur sequi provident architecto dignissimos quia magni! Molestiae, illum sapiente.
                </p>
                <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Placeat saepe voluptates, pariatur, deserunt, assumenda cumque consectetur numquam non cum enim quia reprehenderit. Explicabo voluptatibus, illo ea temporibus provident iste est repudiandae quam aperiam sit, blanditiis ad, ipsam laborum facere nesciunt dolore atque veniam ullam maiores corporis accusamus. Ipsum ad, dignissimos error debitis cumque laboriosam pariatur corrupti quisquam odit voluptatum, illum laudantium delectus. Placeat eligendi quidem quia magnam ut totam odio saepe mollitia veritatis dolor officiis consectetur nobis, est expedita alias eum adipisci consequuntur ab laudantium natus repellendus. Tenetur possimus est voluptatum blanditiis consequuntur temporibus ea doloremque non architecto excepturi vitae, aut quas molestiae ducimus ipsam, asperiores illo ab natus quos, eos rerum eligendi mollitia necessitatibus officia. Asperiores, voluptatibus, quisquam, qui voluptatem labore nulla eum consequatur explicabo optio voluptates praesentium nesciunt tempore. Commodi, itaque. Adipisci, doloribus iure dolor rem deleniti enim sed repellendus laudantium nobis nostrum obcaecati unde doloremque dicta! Iusto, veritatis amet! Culpa, exercitationem? Temporibus quos natus, suscipit praesentium illum ipsam totam excepturi id assumenda possimus rerum nostrum, ex repellat nam tenetur similique veritatis consequuntur sapiente architecto laboriosam distinctio explicabo molestiae atque laborum. Perferendis tenetur ut facilis numquam at eaque odio corporis aperiam, sapiente molestiae possimus officia amet nisi minus excepturi impedit, voluptas accusantium! Perferendis asperiores maiores eveniet excepturi, atque quisquam consequuntur delectus adipisci, sapiente quaerat et. Excepturi, vel repellat architecto perferendis, saepe sapiente enim eveniet ipsum eligendi magni harum fuga vitae. Mollitia, eligendi? Modi reprehenderit non quidem sint asperiores eaque laboriosam, maiores fuga repudiandae distinctio veritatis fugiat ipsum! Praesentium obcaecati aspernatur quibusdam doloribus repellat adipisci vero impedit distinctio, at ea et, ad alias dolorum voluptate atque. Rem eum nihil harum quam explicabo! Animi veniam nihil, hic adipisci ab, incidunt voluptatem soluta magnam corporis accusamus facilis quibusdam temporibus et illo dolorem earum quam voluptate reiciendis placeat odio quidem natus officiis fugit atque? Neque tenetur in ullam quibusdam sed modi adipisci earum voluptatibus dolores veniam, quae dolorem nisi blanditiis eius consequatur, debitis consectetur cumque eligendi, odio accusamus ducimus quaerat. Ipsum minus fuga aspernatur repellat odit a eos nulla molestias ullam exercitationem eligendi, nisi facere illo expedita tenetur quisquam neque assumenda ipsa. Est incidunt sequi qui neque. Inventore iusto deleniti expedita consectetur optio quisquam repellendus quod sed necessitatibus amet, culpa provident, illo natus non quasi possimus nobis dicta? Doloribus repellendus minima modi dolorum. Eius nostrum recusandae iusto voluptates maiores eum, ullam praesentium iste et! Nam consequatur hic placeat perspiciatis beatae odio cumque?
                </p>
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Repellat dolor est incidunt nulla! Voluptatum porro rerum eligendi ut perferendis dolorum nisi ullam laudantium non atque, magni quasi est modi autem consequatur, nihil expedita. Distinctio minus iste excepturi est tempora eos incidunt! Ea mollitia, consequuntur commodi id tempore eligendi sint repellendus quisquam reprehenderit tempora beatae voluptates? Optio dolores porro sit labore error nostrum architecto assumenda autem amet, obcaecati sequi nulla quasi repudiandae itaque unde officia temporibus iste quas deserunt? Suscipit voluptatem illo veritatis error iste necessitatibus quae mollitia culpa. Fugit dolore cum pariatur, eaque, deserunt porro ratione voluptatum et nulla fuga vero! Error, dolorum ea iusto porro quam asperiores molestiae modi quis alias, quibusdam ullam dicta temporibus vitae? Est suscipit error officiis ut autem velit sequi alias enim delectus quis odio, consequuntur quam earum doloribus, esse optio consectetur! Dicta voluptatibus minus iure omnis ea facere, non molestiae expedita laborum labore id ab tempora reiciendis facilis odit illum nulla eos fuga. Similique expedita dolorem saepe sequi tempora officia voluptate nobis quas obcaecati libero assumenda eum eos neque quis omnis animi laudantium dignissimos, commodi inventore fuga! Quam, voluptas laudantium! Obcaecati eligendi porro eaque excepturi, quas in delectus molestias omnis ad non aperiam tempora quidem iusto voluptates sequi, itaque, magnam architecto officia et debitis consequuntur. Ipsam fugit autem reprehenderit dolore exercitationem alias reiciendis nesciunt qui. Corrupti vitae, rem sit, quas illum obcaecati ipsum neque consequatur aliquid id minima non repudiandae dolore dolores dignissimos quibusdam laborum assumenda! Recusandae ipsum reprehenderit, ea veniam pariatur id minus maiores corrupti blanditiis aspernatur repellat impedit eos, placeat reiciendis earum iste velit nulla sequi illo. Vitae et, est, sit tenetur, animi nostrum fugiat dolorum quaerat a voluptas nulla? Laudantium commodi vitae voluptate ducimus iste nulla quaerat dignissimos repudiandae eligendi nemo assumenda ipsum porro voluptatibus, ea dicta ut doloremque. Alias, quod!
            </div>
            <div className='px-4 h-max sticky top-8 min-w-60'>
                <h1 className='mb-4 text-sm font-medium'>Author</h1>
                <div className='flex flex-col gap-4'>
                    <div className='flex items-center gap-4'>
                        <Image src='/tech-blog/userImg.jpeg' className='w-12 h-12 rounded-full object-cover' w='48' h='48' /> 
                        <Link to='/test' className='text-blue-800'>John Doe</Link>
                    </div>
                    <p className='text-sm text-gray-500'>Lorem ipsum dolor sit amet consectetur adipisicing elit.</p>
                    <div className='flex gap-3'>
                        <Link to='/test'>
                            <FontAwesomeIcon icon={faLinkedin} />
                        </Link>
                        <Link to='/test'>
                            <FontAwesomeIcon icon={faGithub} />
                        </Link>
                    </div>
                </div>
                <PostMenuActions />
                <h1 className='mt-8 mb-4 text-sm font-medium'>Categories</h1>
                <div className='flex flex-col gap-2 text-sm'>
                    <Link to='/test' className='underline'>Programming</Link>
                    <Link to='/test' className='underline'>Network Engineering</Link>
                    <Link to='/test' className='underline'>Spring Boot</Link>
                    <Link to='/test' className='underline'>All</Link>
                </div>
                <h1 className='mt-8 mb-4 text-sm font-medium'>Search</h1>
                <Search />
            </div>
        </div>
        <Comments />
    </div>
  )
}

export default SinglePostPage